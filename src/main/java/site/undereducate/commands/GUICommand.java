package site.undereducate.commands;

import com.hakan.invapi.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.invs.Pagination;
import com.hakan.invapi.inventory.item.ClickableItem;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import site.undereducate.UndereducateGUIPlugin;
import site.undereducated.undereducatedutil.UndereducatedAPI;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static site.undereducate.utils.PingManager.getFormatedPing;

public class GUICommand implements CommandExecutor {
	Plugin plugin;
	public GUICommand(UndereducateGUIPlugin instance) {
		plugin = instance;
	}
	static Plugin guiPlugin = Bukkit.getPluginManager().getPlugin("UndereducateGUI");

	@Override
	public boolean onCommand(CommandSender sender, Command command,  String label,String[] args) {
		Player player = (Player) sender;
		if (args.length == 0) {
			// get player
			sendMessageConf("messages.openGUI", player, plugin);
			// Setup inventory
			HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(Bukkit.getOnlinePlayers().size() + " online players").setCloseable(false).setSize(5).setId("b").create();
			Pagination pagination = hInventory.getPagination();
			pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));

			// Setup array
			List<ClickableItem> clickableItemList = new ArrayList<>();
			// for each player
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				// add their head to the list, and an event
				ItemStack item = new ItemStack(getPlayerHead(p));
				clickableItemList.add(ClickableItem.of(item, (event) -> {
					// if it was a left click
					if(event.getClick() == ClickType.LEFT){
						// close inven
						hInventory.close(player);
						// declare a list
						List<String> list = null;
						assert list != null;
						// define the list from the config
						list = (List<String>) plugin.getConfig().getList("gui.execute");
						// for each entry in list
						for (String s : list) {
							// get the display name of the clicked item
							String displayName = Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getDisplayName();
							// replace the player placeholder in the config with the clicked name

							// check displayname
							if(player.getDisplayName().equals(ChatColor.stripColor(displayName))){
								sendMessageConf("messages.selfCommands", player, plugin);
								break;
							}

							String cmd = s.replace("%player%", displayName);
							/// debug message
							sendMessageConf("messages.execCommands", player, plugin);
							// send the message stripped of color
							player.performCommand(ChatColor.stripColor(cmd));
						}
					}
				}));
			}

			// add all the items to paginate
			pagination.setItems(clickableItemList);

			// set back arrow
			hInventory.setItem(38, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
				player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
				pagination.previousPage();
			}));

			// set close barrier
			hInventory.setItem(40, ClickableItem.of(new ItemStack(Material.BARRIER), (event) -> {
				player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
				hInventory.close(player);
			}));

			// set front arrow
			hInventory.setItem(42, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
				player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
				pagination.nextPage();
			}));

			// open the inventory
			hInventory.open(player);
			return true;
		}
		else if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
				if(!player.hasPermission("clientdetection.viewother")){
					player.sendMessage(UndereducatedAPI.process(String.format("%s&cYou do not have permission to reload the configuration", plugin.getConfig().getString("messages.prefix"))));
					return true;
				}
				plugin.reloadConfig();
				player.sendMessage(UndereducatedAPI.process(String.format("%s&7Successfully reloaded configuration", plugin.getConfig().getString("messages.prefix"))));
				UndereducatedAPI.log("Reloaded configuration", "UndereducateGUI");
				return true;
			} else if(args[0].equalsIgnoreCase("config")){
				// open config gui
				return true;
			} else if(args[0].equalsIgnoreCase("help")){
				// show help
				player.sendMessage(UndereducatedAPI.process(String.format("%s &7Help", plugin.getConfig().getString("messages.prefix"))));
				player.sendMessage(UndereducatedAPI.process("&7➸ &fconfig &r | &7enable / disable entries"));
				player.sendMessage(UndereducatedAPI.process("&7➸ &fhelp &r | &7shows this"));
				return true;
			}
		}
		return false;
	}


	// get player head function
	public ItemStack getPlayerHead(Player player){
		// check if the version is a new version, for support with the new heads
		boolean isNewVersion = Arrays.stream(Material.values())
				.map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
		// if new, set the material to PLAYER_HEAD, if not, set the material to SKULL_ITEM
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		// init item
		assert type != null;
		ItemStack item = new ItemStack(type, 1);
		// if not new, set durability
		if(!isNewVersion)
			item.setDurability((short) 3);
		// setup skull meta so we can change the skin
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		// set owning player to the player, this function sets the skin to the players skin.
		meta.setOwningPlayer(player);
		// set the skull meta
		item.setItemMeta(meta);
		// get the meta, but make it an ItemMeta so we can have access to item meta.
		ItemMeta Meta = item.getItemMeta();
		// set the display name to the players display name
		Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f" + player.getName()));
		// if showIfVanished is enabled
		if( plugin.getConfig().getBoolean("gui.showIfVanished")){
			if(isVanished(player)){
				Meta.setDisplayName(UndereducatedAPI.process("<SOLID:98a5aa>[<SOLID:dd574b>V<SOLID:98a5aa>]&f " + player.getName()));
			}
		}
		// set the item meta
		item.setItemMeta(Meta);
		// return the skull
		return addLore(item, player);
	}

	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}

	// set lore
	public ItemStack addLore(ItemStack i, Player p){
		// get ItemMeta from provided ItemStack
		ItemMeta itemMeta = i.getItemMeta();
		// Init list of strings for the lores
		List<String> lores = new ArrayList<String>();
		// if ping enabled
		if( plugin.getConfig().getBoolean("gui.showPing")){
			// add values to the lore
			String ping = Objects.requireNonNull(plugin.getConfig().getString("lores.ping"))
					.replace("%ping%", getFormatedPing(p));
			lores.add(UndereducatedAPI.process(ping));
		}
		// if health enabled
		if( plugin.getConfig().getBoolean("gui.showHealth")){
			// add values to the lore
			Double pHealth = Math.round(p.getHealth() * 100.0) / 100.0;
			String health = Objects.requireNonNull(plugin.getConfig().getString("lores.health"))
					.replace("%health%", String.valueOf(pHealth));
			lores.add(UndereducatedAPI.process(health));
		}
		// if showRank is enabled
		if( plugin.getConfig().getBoolean("gui.showRank")){
			if(UndereducateGUIPlugin.chat.getPlayerPrefix(p) != null){
				if(UndereducateGUIPlugin.chat.getPlayerPrefix(p).length() >= 1){
					String rankStr = Objects.requireNonNull(plugin.getConfig().getString("lores.rank"))
							.replace("%rank%", UndereducateGUIPlugin.chat.getPlayerPrefix(p));
					lores.add(UndereducatedAPI.process(rankStr));
				} else{
					String rankStr = Objects.requireNonNull(plugin.getConfig().getString("lores.rank"))
							.replace("%rank%", "None");
					lores.add(UndereducatedAPI.process(rankStr));
				}
			}
		}
		// if showBalance is enabled
		if( plugin.getConfig().getBoolean("gui.showBalance")){
			String balanceStr = Objects.requireNonNull(plugin.getConfig().getString("lores.balance"))
					.replace("%balance%", String.valueOf(UndereducateGUIPlugin.econ.getBalance(p)));
			String balance = UndereducatedAPI.process(balanceStr);
			lores.add(balance);
		}
		// if showClient is enabled
		if( plugin.getConfig().getBoolean("gui.showClient")){
			String clientStr = plugin.getConfig().getString("lores.client");
			try{
				clientStr = PlaceholderAPI.setPlaceholders(p, clientStr);
				if(clientStr != null)
					lores.add(UndereducatedAPI.process(clientStr));
			} catch(NullPointerException exception){
				lores.add(UndereducatedAPI.process("<SOLID:92d68b>Client: Unable to fetch"));
				UndereducatedAPI.log(String.format("There was an error fetching the client version of %s", p.getDisplayName()), "UndereducateGUI");
			}
		}

		// set the lore
		itemMeta.setLore(lores);
		// set the meta
		i.setItemMeta(itemMeta);
		return i;
	}


	// Send message from config
	public static void sendMessageConf(String configVar, Player reciever, Plugin plugin){
		String msg = Objects.requireNonNull(plugin.getConfig().getString(configVar))
				.replace("%prefix%",
						Objects.requireNonNull(plugin.getConfig().getString("messages.prefix")));
		reciever.sendMessage(UndereducatedAPI.process(msg));
	}

}
