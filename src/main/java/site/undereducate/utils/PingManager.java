package site.undereducate.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PingManager {

	public static int getPing(Player p) {
		try {
			Object entityPlayer = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			System.out.println(entityPlayer.getClass().getField("ping").get(entityPlayer));
			return (Integer) entityPlayer.getClass().getField("ping").get(entityPlayer);
		} catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getFormatedPing(Player p){
		int ping = getPing(p);
		return formatPing(ping);
	}

	public static String formatPing(Integer ping) {
		if (ping.intValue() < 100){
			return ChatColor.translateAlternateColorCodes('&', "&a" + ping);
		}
		if (ping.intValue() < 200){
			return ChatColor.translateAlternateColorCodes('&', "&e" + ping);
		}
		if (ping.intValue() < 350){
			return ChatColor.translateAlternateColorCodes('&', "&c" + ping);
		}
		return ChatColor.translateAlternateColorCodes('&', "&4" + ping);
	}



}
