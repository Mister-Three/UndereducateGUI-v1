package site.undereducate;

import com.hakan.invapi.InventoryAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import site.undereducate.commands.GUICommand;
import site.undereducated.undereducatedutil.UndereducatedAPI;

public final class UndereducateGUIPlugin extends JavaPlugin {

	public static Economy econ = null;
	public static Permission perms = null;
	public static Chat chat = null;

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

		// Setup Economy, if enabled.
		if(this.getConfig().getBoolean("gui.showBalance")){
			setupEconomy();
		}
		// Setup Vault, if enabled.
		if(this.getConfig().getBoolean("gui.showRank")){
			setupPermissions();
			setupChat();
		}

	}


	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			UndereducatedAPI.log("You have showBalance enabled, but Vault doesn't seem to be installed.", "UndereducatedGUI");
			return false;
		}
		@Nullable RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		UndereducatedAPI.log(" [Vault Economy] Successfully initialized", "UndereducatedGUI");
		return econ != null;
	}

	private boolean setupPermissions() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			UndereducatedAPI.log("You have showRank enabled, but Vault doesn't seem to be installed.", "UndereducatedGUI");
			return false;
		}
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		UndereducatedAPI.log(" [Vault Permissions] Successfully initialized", "UndereducatedGUI");
		return true;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}
}
