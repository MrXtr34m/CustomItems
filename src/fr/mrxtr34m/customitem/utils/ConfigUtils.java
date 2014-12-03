package fr.mrxtr34m.customitem.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrxtr34m.customitem.Main;

public class ConfigUtils {

	public static Main plugin;
	public ConfigUtils(Main plugin) {
		ConfigUtils.plugin = plugin;
	}
	
	public static ItemStack getItemStack(String name, FileConfiguration config){
		config = plugin.getConfig();
		ItemStack is = getRawItem(name, config);
		ItemMeta im = is.getItemMeta();
		if(hasDisplayName(name, config))im = setName(name, config, im);
		if(hasLore(name, config))im=setLores(name, config, im);
		if(hasEnchantment(name, config))im = setEnchant(name, config, im);
		if(im != null)is.setItemMeta(im);
		return is;
	}
	public static void saveItem(String name, ItemStack item, Player p, FileConfiguration config){
		if(config.contains(name)){
			Utils.sendMsg(p, "The item "+name+" has been overwrited");
			config.set(name, null);
		}
		config.set(name+".material", item.getType().name());
		config.set(name+".id", item.getDurability());
		if(item.hasItemMeta()){
			ItemMeta im = item.getItemMeta();
			if(im.hasDisplayName()){
				config.set(name+".name", im.getDisplayName().replace('§', '&'));
			}
			if(im.hasLore()){
				config.set(name+".lore", convertLore(im));
			}
			if(im.hasEnchants()){
				config.set(name+".enchantment", convertEnchant(im));
			}
		}
		Utils.sendMsg(p, "Done !");
		plugin.saveConfig();
	}
	public static List<String> convertEnchant(ItemMeta im){
		List<String> list = new ArrayList<>();
		for(Entry<Enchantment, Integer> ench : im.getEnchants().entrySet()){
			list.add(ench.getKey().getName()+":"+ench.getValue());
		}
		return list;
	}
	public static List<String> convertLore(ItemMeta im){
		List<String> lore = new ArrayList<>();
		for(String imLore : im.getLore()){
			lore.add(imLore.replace('§', '&'));
		}
		return lore;
	}
	public static ItemStack getRawItem(String name, FileConfiguration config){
		ItemStack im = null;
		String materialName = config.getString(name+".material");
		short id = (short) config.getInt(name+".id");
		im = new ItemStack(Material.getMaterial(materialName));
		im.setDurability(id);
		return im;
	}
	public static boolean hasLore(String name, FileConfiguration config){
		return config.contains(name+".lore");
	}
	public static boolean hasDisplayName(String name, FileConfiguration config){
		return config.contains(name+".name");
	}
	public static boolean hasEnchantment(String name, FileConfiguration config) {
		return config.contains(name+".enchantment");
	}
	public static ItemMeta setEnchant(String name, FileConfiguration config, ItemMeta im){
	List<String> raw = config.getStringList(name+".enchantment");
	for (int i = 0; i < raw.size(); i++) {
		String[] lel = raw.get(i).split(":");
		Enchantment enchant = Enchantment.getByName(lel[0]);
		im.addEnchant(enchant, Integer.parseInt(lel[1]), true);
	}
	return im;
	}
	public static ItemMeta setName(String name, FileConfiguration config,ItemMeta im){
		String item =config.getString(name+".name");
		if(item != null)im.setDisplayName(ChatColor.translateAlternateColorCodes('&', item));
		return im;
	}
	public static ItemMeta setLores(String name, FileConfiguration config, ItemMeta im){
		List<String> lore = config.getStringList(name+".lore");
		ArrayList<String> finalLore = new ArrayList<String>();
		for(String str : lore){
			finalLore.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		im.setLore(finalLore);
		return im;
	}
}
