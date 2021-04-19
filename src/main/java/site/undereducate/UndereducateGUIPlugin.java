package site.undereducate;

import com.hakan.invapi.InventoryAPI;
import org.bukkit.plugin.java.JavaPlugin;
import site.undereducate.commands.GUICommand;
import site.undereducated.undereducatedutil.UndereducatedAPI;

public final class UndereducateGUIPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getCommand("onlinegui").setExecutor(new GUICommand(this));
		try {
			UndereducatedAPI.loadPlugin("UndereducateGUI");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// load config
		getConfig().options().copyDefaults();
		saveDefaultConfig();

		// Setup InvAPI
		InventoryAPI.setup(this);
	}
}
