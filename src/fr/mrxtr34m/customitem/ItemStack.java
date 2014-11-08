package fr.mrxtr34m.customitem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemStack {
	
	private String item;
	private Main plugin;
	
	public ItemStack(Main plugin, String itemName) {
		this.item = itemName;
		this.plugin = plugin;	
		}
	public String getName(){
		return item;
	}
	public org.bukkit.inventory.ItemStack getItemStack(){
		try {
			org.bukkit.inventory.ItemStack itemstack = plugin.getConfig().getItemStack(item);
			return itemstack;
		} catch (Exception e) {
			e.printStackTrace();
			return new org.bukkit.inventory.ItemStack(Material.AIR);
		}
	}
}
