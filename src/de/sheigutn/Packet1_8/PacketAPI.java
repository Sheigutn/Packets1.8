package de.sheigutn.Packet1_8;

import com.avaje.ebeaninternal.server.cluster.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import javassist.*;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;

import static com.comphenix.protocol.ProtocolLibrary.*;
import static com.comphenix.protocol.PacketType.UNKNOWN_PACKET;

public class PacketAPI extends JavaPlugin implements Listener{
   // private static ArrayList<String> messages = new ArrayList<>();
    private static final int CHAT = 0x02;
    private static final int CAMERA = 0x43;
    private static final int WORLD_BORDER = 0x44;
    private static final int TITLE = 0x45;
    private static final int TABLIST = 0x47;
    private static final PacketType chatPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, CHAT, UNKNOWN_PACKET);
    private static final PacketType cameraPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, CAMERA, UNKNOWN_PACKET);
    private static final PacketType borderPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, WORLD_BORDER, UNKNOWN_PACKET);
    private static final PacketType titlePacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, TITLE, UNKNOWN_PACKET);
    private static final PacketType tablistPacket = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, TABLIST, UNKNOWN_PACKET);
    private static PacketAPI instance;
    //private static HashMap<UUID, Integer> players = new HashMap<UUID, Integer>();
    //private static Field channelField;
    //private static HashMap<UUID, Channel> channels = new HashMap<>();
    public void onEnable() {
       /* try {
            channelField = NetworkManager.class.getDeclaredField("m");
            channelField.setAccessible(true);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }*/
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
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        /*for(Player p : Bukkit.getOnlinePlayers())
        {
            addChannel(p);
        }*/
    }

    @EventHandler
    public void onKick(PlayerKickEvent ev)
    {
        if(ev.getReason().equalsIgnoreCase("Cannot interact with self!"))
        {
            ev.setCancelled(true);
            getLogger().log(Level.INFO, "Cancelled");
        }
    }

    /*@EventHandler
    public void onDeath(EntityDeathEvent ev)
    {
        getLogger().log(Level.INFO, "Stimmt");
        if(players.containsValue(ev.getEntity().getEntityId()))
        {
            getLogger().log(Level.INFO, "Stimmt");
            for(Iterator<Map.Entry<UUID, Integer>> it = players.entrySet().iterator(); it.hasNext();)
            {
                getLogger().log(Level.INFO, "Stimmt");
                Map.Entry<UUID, Integer> player = it.next();
                if(player.getValue().equals(ev.getEntity().getEntityId()))
                {
                    try {
                        resetCamera(getServer().getPlayer(player.getKey()));
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    it.remove();
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof LivingEntity)
        {
            try {
                players.put(event.getPlayer().getUniqueId(), event.getRightClicked().getEntityId());
                switchCamera(event.getPlayer(), (LivingEntity) event.getRightClicked());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent ev)
    {
        if(players.containsKey(ev.getPlayer().getUniqueId()))
        {
            Player p = ev.getPlayer();
            try{
                resetCamera(p);
                players.remove(ev.getPlayer().getUniqueId());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent ev)
    {
        players.remove(ev.getPlayer().getUniqueId());
    }
    */
    /*public void addChannel(Player p){
        try {
            if(channels.get(p.getUniqueId()) == null) {
                Channel c = (Channel) channelField.get(((CraftPlayer) p).getHandle().playerConnection.networkManager);
                channels.put(p.getUniqueId(), c);
                if(c.pipeline().names().contains("PacketAPI"))
                {
                    c.pipeline().remove("PacketAPI");
                }
                c.pipeline().addBefore("packet_handler", "PacketAPI", new CustomPacketOutboundHandler());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

   /* public void removeChannel(Player p)
    {
        Channel c = channels.remove(p.getUniqueId());
        if(c.pipeline().names().contains("PacketAPI")) c.pipeline().remove("PacketAPI");
    }*/

    public static PacketAPI getInstance()
    {
        return instance;
    }

    public void switchCamera(Player p, LivingEntity entity) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(cameraPacket, new PacketPlayOutCamera(entity.getEntityId())));
    }

    public void resetCamera(Player p) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(cameraPacket, new PacketPlayOutCamera(p.getEntityId())));
    }

    public void setTitleWithAnimations(Player p, PacketPlayOutTitle.Action action, String json, int fadeIn, int stay, int fadeOut) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(action, json, fadeIn, stay, fadeOut)));
    }

    public void clearTitle(Player p) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(PacketPlayOutTitle.Action.CLEAR, null)));
    }
    public void resetTitle(Player p) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(PacketPlayOutTitle.Action.RESET, null)));
    }

    public void setTitle(Player p, PacketPlayOutTitle.Action action, String json) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(titlePacket, new PacketPlayOutTitle(action, json)));
    }

    public void setTabListHeaderAndFooter(Player p, String header, String footer) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(tablistPacket, new PacketPlayOutTabListHeaderFooter(header, footer)));
    }

    public void sendWorldBorder(Player p, double x, double z, double oldRadius, double newRadius, long speed) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(x, z, oldRadius, newRadius, speed,-1,-1,-1)));
    }

    public void setWorldBorderCenter(Player p, double x, double z) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(x,z)));
    }
    public void setWorldBorderSize(Player p, double radius) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(radius)));
    }

    public void changeWorldBorderSize(Player p, double oldSize, double newSize) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(Double.valueOf(oldSize), Double.valueOf(newSize))));
    }


    public void setWorldBorderWarningTime(Player p, int warningTime) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(PacketPlayOutWorldBorder.Action.SET_WARNING_TIME, warningTime, -1)));
    }


    public void setWorldBorderWarningBlocks(Player p, int warningBlocks) throws InvocationTargetException
    {
            getProtocolManager().sendServerPacket(p, new PacketContainer(borderPacket, new PacketPlayOutWorldBorder(PacketPlayOutWorldBorder.Action.SET_WARNING_BLOCKS,-1,warningBlocks)));
    }
}
