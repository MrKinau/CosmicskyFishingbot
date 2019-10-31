/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.bot;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.Setter;
import systems.kinau.fishingbot.FishingBot;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.PosLookChangeEvent;
import systems.kinau.fishingbot.event.play.SetHeldItemEvent;
import systems.kinau.fishingbot.event.play.UpdateExperienceEvent;
import systems.kinau.fishingbot.event.play.UpdateSlotEvent;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;
import systems.kinau.fishingbot.network.protocol.play.PacketOutTeleportConfirm;

public class Player implements Listener {

    @Getter @Setter private double x;
    @Getter @Setter private double y;
    @Getter @Setter private double z;
    @Getter @Setter private float yaw;
    @Getter @Setter private float pitch;

    @Getter @Setter private int experience;
    @Getter @Setter private int levels;

    @Getter @Setter private int heldSlot;
    @Getter @Setter private ByteArrayDataOutput slotData;

    public Player() {
        FishingBot.getInstance().getEventManager().registerListener(this);
    }

    @EventHandler
    public void onPosLookChange(PosLookChangeEvent event) {
        this.x = event.getX();
        this.y = event.getY();
        this.z = event.getZ();
        this.yaw = event.getYaw();
        this.pitch = event.getPitch();
        if (FishingBot.getInstance().getServerProtocol() >= ProtocolConstants.MINECRAFT_1_9)
            FishingBot.getInstance().getNet().sendPacket(new PacketOutTeleportConfirm(event.getTeleportId()));

    }

    @EventHandler
    public void onUpdateXP(UpdateExperienceEvent event) {
        this.levels = event.getLevel();
        this.experience = event.getExperience();
    }

    @EventHandler
    public void onSetHeldItem(SetHeldItemEvent event) {
        this.heldSlot = event.getSlot();
    }

    @EventHandler
    public void onUpdateSlot(UpdateSlotEvent event) {
        if(event.getWindowId() != 0)
            return;
        if(event.getSlotId() != getHeldSlot())
            return;
        this.slotData = event.getSlotData();
    }
}
