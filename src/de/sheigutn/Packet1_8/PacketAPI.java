package de.sheigutn.Packet1_8;

import net.minecraft.server.v1_7_R4.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PacketAPI extends JavaPlugin implements Listener {

	@SuppressWarnings("unused")
	private static final int CHAT = 0x02;
	private static final int CAMERA = 0x43;
	private static final int WORLD_BORDER = 0x44;
	private static final int TITLE = 0x45;
	private static final int TABLIST = 0x47;
	private static PacketAPI instance;

	public void onEnable() {
		registerPacket(EnumProtocol.PLAY, PacketPlayOutWorldBorder.class,
				WORLD_BORDER, true);
		registerPacket(EnumProtocol.PLAY, PacketPlayOutCamera.class, CAMERA,
				true);
		registerPacket(EnumProtocol.PLAY,
				PacketPlayOutTabListHeaderFooter.class, TABLIST, true);
		registerPacket(EnumProtocol.PLAY, PacketPlayOutTitle.class, TITLE, true);
		instance = this;
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	@SuppressWarnings("unchecked")
	private void registerPacket(EnumProtocol protocol,
			Class<? extends Packet> packetClass, int packetID,
			boolean isClientbound) {
		try {
			if (isClientbound) {
				protocol.b().put(packetID, packetClass);
			} else {
				protocol.a().put(packetID, packetClass);
			}
			Field mapField = EnumProtocol.class.getDeclaredField("f");
			mapField.setAccessible(true);
			Map<Class<? extends Packet>, EnumProtocol> map = (Map<Class<? extends Packet>, EnumProtocol>) mapField
					.get(null);
			map.put(packetClass, protocol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Listener **/
	@EventHandler
	public void onKick(PlayerKickEvent ev) {
		if (ev.getReason().equalsIgnoreCase("Cannot interact with self!")) {
			ev.setCancelled(true);
		}
	}

	/** Methods **/
	public static PacketAPI getInstance() {
		return instance;
	}

	private void sendPacket(Player p, Object packet) {
		try {
			Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
			Object connection = nmsPlayer.getClass()
					.getField("playerConnection").get(nmsPlayer);
			for (Method m : connection.getClass().getMethods()) {
				if (m.getParameterTypes().length == 1
						&& m.getName().equalsIgnoreCase("sendPacket")) {
					m.setAccessible(true);
					m.invoke(connection, packet);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchCamera(Player p, LivingEntity entity)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutCamera(entity.getEntityId()));
	}

	public void resetCamera(Player p) throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutCamera(p.getEntityId()));
	}

	public void setTitleWithAnimations(Player p,
			PacketPlayOutTitle.Action action, String json, int fadeIn,
			int stay, int fadeOut) throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutTitle(action, json, fadeIn, stay,
				fadeOut));
	}

	public void clearTitle(Player p) throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutTitle(PacketPlayOutTitle.Action.CLEAR,
				null));
	}

	public void resetTitle(Player p) throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutTitle(PacketPlayOutTitle.Action.RESET,
				null));
	}

	public void setTitle(Player p, PacketPlayOutTitle.Action action, String json)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutTitle(action, json));
	}

	public void setTabListHeaderAndFooter(Player p, String header, String footer)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutTabListHeaderFooter(header, footer));
	}

	public void sendWorldBorder(Player p, double x, double z, double oldRadius,
			double newRadius, long speed) throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(x, z, oldRadius, newRadius,
				speed, -1, -1, -1));
	}

	public void setWorldBorderCenter(Player p, double x, double z)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(x, z));
	}

	public void setWorldBorderSize(Player p, double radius)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(radius));
	}

	public void changeWorldBorderSize(Player p, double oldSize, double newSize)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(Double.valueOf(oldSize),
				Double.valueOf(newSize)));
	}

	public void setWorldBorderWarningTime(Player p, int warningTime)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(
				PacketPlayOutWorldBorder.Action.SET_WARNING_TIME, warningTime,
				-1));
	}

	public void setWorldBorderWarningBlocks(Player p, int warningBlocks)
			throws InvocationTargetException {
		sendPacket(p, new PacketPlayOutWorldBorder(
				PacketPlayOutWorldBorder.Action.SET_WARNING_BLOCKS, -1,
				warningBlocks));
	}
}
