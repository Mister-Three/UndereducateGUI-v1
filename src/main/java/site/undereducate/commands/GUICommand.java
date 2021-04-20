package site.undereducate.commands;

import com.hakan.invapi.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.invs.Pagination;
import com.hakan.invapi.inventory.item.ClickableItem;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.undereducate.UndereducateGUIPlugin;
import site.undereducated.undereducatedutil.UndereducatedAPI;

import java.lang.reflect.InvocationTargetException;
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
		// get player
		Player player = (Player) sender;

		// Setup inventory
		HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(Bukkit.getOnlinePlayers().size() + " online players").setCloseable(false).setSize(5).setId("b").create();
		Pagination pagination = hInventory.getPagination();
		pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));

		// Setup array
		List<ClickableItem> clickableItemList = new ArrayList<>();
		// for each player
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			// add their head to the list, and an event
			clickableItemList.add(ClickableItem.of(new ItemStack(getPlayerHead(p)), (event) -> {
				hInventory.close(p);
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

	// get player head function
	public ItemStack getPlayerHead(Player player){
		// check if the version is a new version, for support with the new heads
		boolean isNewVersion = Arrays.stream(Material.values())
				.map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
		// if new, set the material to PLAYER_HEAD, if not, set the material to SKULL_ITEM
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		// init item
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
		// if showRank is enabled
		if( plugin.getConfig().getBoolean("gui.showRank")){
			String rankStr = plugin.getConfig().getString("lores.rank")
					.replace("%rank%", UndereducateGUIPlugin.chat.getPlayerPrefix(p));
			lores.add(UndereducatedAPI.process(rankStr));
		}
		// if ping enabled
		if( plugin.getConfig().getBoolean("gui.showPing")){
			// add values to the lore
			String ping = plugin.getConfig().getString("lores.ping")
					.replace("%ping%", getFormatedPing(p));
			lores.add(UndereducatedAPI.process(ping));
		}
		// if showBalance is enabled
		if( plugin.getConfig().getBoolean("gui.showBalance")){
			String balanceStr = plugin.getConfig().getString("lores.balance")
					.replace("%balance%", String.valueOf(UndereducateGUIPlugin.econ.getBalance(p)));
			String balance = UndereducatedAPI.process(balanceStr);
			lores.add(balance);
		}
		// if showClient is enabled
		if( plugin.getConfig().getBoolean("gui.showClient")){
			String clientStr = plugin.getConfig().getString("lores.client");
			clientStr = PlaceholderAPI.setPlaceholders(p, clientStr);
			lores.add(UndereducatedAPI.process(clientStr));
		}
		// set the lore
		itemMeta.setLore(lores);
		// set the meta
		i.setItemMeta(itemMeta);
		return i;
	}
}
