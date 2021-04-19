package site.undereducate.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem  extends ItemStack {

	public CustomItem(Material material){
		this.setType(material);
		this.setAmount(1);
	}


	public CustomItem(Material material, Integer count){
		this.setType(material);
		this.setAmount(count);
	}

	public CustomItem(Material material, String displayName){
		this.setType(material);
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(displayName);
		this.setItemMeta(im);
		this.setAmount(1);
	}

	public CustomItem(Material material, List<String> lores){
		this.setType(material);
		ItemMeta im = this.getItemMeta();
		im.setLore(lores);
		this.setItemMeta(im);
		this.setAmount(1);
	}

	public CustomItem(Material material, Integer count, String displayName){
		this.setType(material);
		this.setAmount(count);
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(displayName);
		this.setItemMeta(im);

	}

	public CustomItem(Material material, Integer count, List<String> lores){
		this.setType(material);
		this.setAmount(count);
		ItemMeta im = this.getItemMeta();
		im.setLore(lores);
		this.setItemMeta(im);
	}

	public CustomItem(Material material, String displayName, List<String> lores){
		this.setType(material);
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(displayName);
		im.setLore(lores);
		this.setItemMeta(im);
		this.setAmount(1);
	}

	public CustomItem(Material material, Integer count, String displayName, List<String> lores){
		this.setType(material);
		this.setAmount(count);
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(displayName);
		im.setLore(lores);
		this.setItemMeta(im);
	}

	public void add(Player player){
		player.getInventory().addItem(this);
	}

	public void add(Inventory inventory){
		inventory.addItem(this);
	}

	public void set(Player player, Integer slot){
		player.getInventory().setItem(slot, this);
	}

	public void set(Inventory inventory, Integer slot){
		inventory.setItem(slot, this);
	}

}



