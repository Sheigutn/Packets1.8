package de.sheigutn.Packet1_8;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import net.minecraft.server.v1_7_R4.EnumProtocol;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static com.comphenix.protocol.ProtocolLibrary.*;
import static com.comphenix.protocol.PacketType.UNKNOWN_PACKET;

public class PacketAPI extends JavaPlugin{
    private static final int CAMERA = 0x43;
    private static final int WORLD_BORDER = 0x44;
    private static final int TITLE = 0x45;
    private static final int TABLIST = 0x47;
    private static final PacketType cameraPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, CAMERA, UNKNOWN_PACKET);
    private static final PacketType borderPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, WORLD_BORDER, UNKNOWN_PACKET);
    private static final PacketType titlePacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, TITLE, UNKNOWN_PACKET);
    private static final PacketType tablistPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, TABLIST, UNKNOWN_PACKET);
    private static PacketAPI instance;
    public void onEnable(){
        EnumProtocol.PLAY.b().put(CAMERA, PacketPlayOutCamera.class);
        EnumProtocol.PLAY.b().put(WORLD_BORDER, PacketPlayOutWorldBorder.class);
        EnumProtocol.PLAY.b().put(TITLE, PacketPlayOutTitle.class);
        EnumProtocol.PLAY.b().put(TABLIST, PacketPlayOutTabListHeaderFooter.class);
        Map<Class<?>, EnumProtocol> map = (Map<Class<?>, EnumProtocol>)
                Accessors.getFieldAccessor(EnumProtocol.class, Map.class, true).get(EnumProtocol.PLAY);
        map.put(PacketPlayOutCamera.class, EnumProtocol.PLAY);
        map.put(PacketPlayOutWorldBorder.class, EnumProtocol.PLAY);
        map.put(PacketPlayOutTitle.class, EnumProtocol.PLAY);
        map.put(PacketPlayOutTabListHeaderFooter.class, EnumProtocol.PLAY);
        instance = this;
    }

    public static PacketAPI getInstance()
    {
        return instance;
    }

    public void switchCamera(Player p, LivingEntity entity) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(cameraPacket, new PacketPlayOutCamera(entity.getEntityId())));
    }

    public void resetCamera(Player p) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(cameraPacket, new PacketPlayOutCamera(p.getEntityId())));
    }

    public void setTitleWithAnimations(Player p, PacketPlayOutTitle.Action action, String json, int fadeIn, int stay, int fadeOut) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(action, json, fadeIn, stay, fadeOut)));
    }

    public void clearTitle(Player p) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(PacketPlayOutTitle.Action.CLEAR, null)));
    }
    public void resetTitle(Player p) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(PacketPlayOutTitle.Action.RESET, null)));
    }

    public void setTitle(Player p, PacketPlayOutTitle.Action action, String json) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(action, json)));
    }

    public void setTabListHeaderAndFooter(Player p, String header, String footer) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(tablistPacket, new PacketPlayOutTabListHeaderFooter(header, footer)));
    }

    public void sendWorldBorder(Player p, double x, double z, double oldRadius, double newRadius, long speed) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(x, z, oldRadius, newRadius, speed,-1,-1,-1)));
    }

    public void setWorldBorderCenter(Player p, double x, double z) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(x,z)));
    }
    public void setWorldBorderSize(Player p, double radius) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(radius)));
    }

    public void changeWorldBorderSize(Player p, double oldSize, double newSize) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(Double.valueOf(oldSize), Double.valueOf(newSize))));
    }


    public void setWorldBorderWarningTime(Player p, int warningTime) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(PacketPlayOutWorldBorder.Action.SET_WARNING_TIME, warningTime, -1)));
    }


    public void setWorldBorderWarningBlocks(Player p, int warningBlocks) throws Exception
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(PacketPlayOutWorldBorder.Action.SET_WARNING_BLOCKS,-1,warningBlocks)));
    }
}
