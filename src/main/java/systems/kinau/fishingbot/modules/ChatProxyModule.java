/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import systems.kinau.fishingbot.FishingBot;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.ChatEvent;
import systems.kinau.fishingbot.network.protocol.play.PacketOutChat;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ChatProxyModule extends Module implements Listener {

    private Thread chatThread;
    private Pattern pattern = Pattern.compile("(.*It got away.*)|(.*you caught.*)");

    @Override
    public void onEnable() {
        FishingBot.getInstance().getEventManager().registerListener(this);
        chatThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(!chatThread.isInterrupted()){
                String line = scanner.nextLine();
                FishingBot.getInstance().getNet().sendPacket(new PacketOutChat(line));
            }
        });
        chatThread.start();
    }

    @Override
    public void onDisable() {
        chatThread.interrupt();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (isEnabled() && !"".equals(event.getText()) /*&& pattern.matcher(event.getText()).matches()*/)
            FishingBot.getLog().info("[CHAT] " + event.getText());
    }
}
