package site.undereducate.events;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import site.undereducate.UndereducateGUIPlugin;

public class onTabCompleteListener implements Listener {
	private final UndereducateGUIPlugin plugin;
	public onTabCompleteListener(UndereducateGUIPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent e) {
		e.setCompletions(Arrays.asList("help", "reload", "config"));
	}

}
