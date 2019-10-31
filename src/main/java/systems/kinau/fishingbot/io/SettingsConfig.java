/*
 * Created by David Luedtke (MrKinau)
 * 2019/8/29
 */
package systems.kinau.fishingbot.io;

import lombok.Getter;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;

@Getter
public class SettingsConfig implements Config {

    @Property(key = "server-ip") private String serverIP = "cosmicsky.com";
    @Property(key = "server-port") private int serverPort = 25565;
    @Property(key = "auto-reconnect") private boolean autoReconnect = true;
    @Property(key = "auto-reconnect-time") private int autoReconnectTime = 3;

    @Property(key = "online-mode") private boolean onlineMode = true;

    @Property(key = "account-username") private String userName = "FishingBot";
    @Property(key = "account-password") private String password = "CHANGEME";

    @Property(key = "log-count") private int logCount = 15;
    @Property(key = "log-packets") private boolean logPackets = false;
    @Property(key = "start-text-enabled") private boolean startTextEnabled = true;
    @Property(key = "start-text") private String startText = "%prefix%Starting fishing;/trigger Bot";
    @Property(key = "proxy-chat") private boolean proxyChat = false;

    @Property(key = "default-protocol") private String defaultProtocol = ProtocolConstants.getVersionString(ProtocolConstants.MINECRAFT_1_12_2);

    @Property(key = "discord-webHook") private String webHook = "false";

    @Property(key = "pathToLake") private String pathToLake = "walkToLake.packets";

    public SettingsConfig() {
        String comments =
                "path-to-lake:\tPossible paths the bot uses to walk from Fisherman to lake\nPossible: walkToLake.packets, walkToLake2.packets, walkToLake3.packets, walkToLake4.packets, walkToLake5.packets\n\n\n" +
                "server-ip:\tServer IP the bot connects to\n" +
                "#server-port:\tPort of the server the bot connects to\n" +
                "#auto-reconnect:\tAuto-Reconnect if bot get kicked/time out etc\n" +
                "#auto-reconnect-time:\tThe time (in seconds) the bot waits after kick to reconnect (only usable if auto-reconnect is set to true)\n" +
                "#online-mode:\tToggles online-mode\n" +
                "#log-count:\t\t\t\tThe number of logs the bot generate\n" +
                "#discord-webHook:\tUse this to send all chat messages from the bot to a Discord webhook\n" +
                "#start-text-enabled:\tIf disabled, the start-text will not be displayed\n" +
                "#start-text:\tChat messages/commands separated with a semicolon\n" +
                "#account-username:\tThe username / e-mail of the account\n" +
                "#account-password:\tThe password of the account (ignored in offline-mode)\n" +
                "#default-protocol:\tOnly needed for Multi-Version servers. The Minecraft-Version for the ping request to the server. Possible values: (1.8, 1.9, 1.9.2, 1.9.2, 1.9.4, ...)\n" +
                "#proxy-chat:\tWhether to function as a chat client (printing incoming chat messages to the console and sending input as chat)";

        init("config.properties", comments);
    }
}
