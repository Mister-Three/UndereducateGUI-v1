package site.undereducate;

import org.bukkit.plugin.java.JavaPlugin;
import site.undereducated.undereducatedutil.UndereducatedAPI;

public final class UndereducateGUIPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		try {
			UndereducatedAPI.loadPlugin("ClientDetection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// load config
		getConfig().options().copyDefaults();
		saveDefaultConfig();
	}
}
