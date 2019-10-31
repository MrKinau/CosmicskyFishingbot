/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import lombok.Getter;
import systems.kinau.fishingbot.FishingBot;
import systems.kinau.fishingbot.bot.Player;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.DifficultySetEvent;
import systems.kinau.fishingbot.event.play.DisconnectEvent;
import systems.kinau.fishingbot.event.play.JoinGameEvent;
import systems.kinau.fishingbot.event.play.KeepAliveEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.play.PacketOutChat;
import systems.kinau.fishingbot.network.protocol.play.PacketOutClientSettings;
import systems.kinau.fishingbot.network.protocol.play.PacketOutKeepAlive;
import systems.kinau.fishingbot.network.protocol.play.PacketOutPosition;

import java.util.Arrays;

public class ClientDefaultsModule extends Module implements Listener {

    @Getter private Thread positionThread;

    @Override
    public void onEnable() {
        FishingBot.getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        positionThread.interrupt();
    }

    @EventHandler
    public void onSetDifficulty(DifficultySetEvent event) {
        if (FishingBot.getInstance().isCosmicSky())
            return;
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Send start texts
            if(FishingBot.getInstance().getConfig().isStartTextEnabled()) {
                Arrays.asList(FishingBot.getInstance().getConfig().getStartText().split(";")).forEach(s -> {
                    FishingBot.getInstance().getNet().sendPacket(new PacketOutChat(s.replace("%prefix%", FishingBot.PREFIX)));
                });
            }

            //Start position updates
            startPositionUpdate(FishingBot.getInstance().getNet());
        }).start();
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        FishingBot.getLog().info("Disconnected: " + event.getDisconnectMessage());
        FishingBot.getInstance().setRunning(false);
    }

    @EventHandler
    public void onJoinGame(JoinGameEvent event) {
        FishingBot.getInstance().getNet().sendPacket(new PacketOutClientSettings());
    }

    @EventHandler
    public void onKeepAlive(KeepAliveEvent event) {
        FishingBot.getInstance().getNet().sendPacket(new PacketOutKeepAlive(event.getId()));
    }

    private void startPositionUpdate(NetworkHandler networkHandler) {
        if(positionThread != null)
            positionThread.interrupt();
        positionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Player player = FishingBot.getInstance().getPlayer();
                networkHandler.sendPacket(new PacketOutPosition(player.getX(), player.getY(), player.getZ(), true));
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
            }
        });
        positionThread.start();
    }
}
