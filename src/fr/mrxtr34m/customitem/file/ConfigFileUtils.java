package fr.mrxtr34m.customitem.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrxtr34m.customitem.Main;
import fr.mrxtr34m.customitem.utils.Utils;

public class ConfigFileUtils {
	public static Main plugin;
	public static void setMain(Main plugin){
		ConfigFileUtils.plugin = plugin;
	}
	public static HashMap<Integer, ItemStack> getItemStack(FileConfiguration config){
		List<String> inv = config.getStringList("items");
	HashMap<Integer, ItemStack> hash = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < inv.size(); i++) {
			String name = getItemName(i, inv);Bukkit.broadcastMessage(name);
			if(name != null || name != ""){
				hash.put(i, getItemStack(name, config, i, inv));
			}
		}
		return hash;
	}
	private static String getItemName(int i, List<String> inv) {
	String g = inv.get(i);
	String[] table = g.split(":");
	return table[1];
	}
	public static ItemStack getItemStack(String name, FileConfiguration config, int i, List<String> inv){
		if(hasItemMeta(i, inv)){
		ItemStack is = getRawItem(name, config);
		ItemMeta im = is.getItemMeta();
		if(hasDisplayName(name, config))im = setName(name, config, im);
		if(hasLore(name, config))im=setLores(name, config, im);
		if(hasEnchantment(name, config))im = setEnchant(name, config, im);
		if(im != null)is.setItemMeta(im);
		return is;
		}else return getItemWithoutMeta(i, config, inv);
	}
	public static void saveInventory(FileConfiguration config, Player p,ConfigFile m){
		List<String> inv = config.getStringList("items");
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			ItemStack items = p.getInventory().getItem(i);
			if(items != null){
				if(!items.getType().equals(Material.AIR)){
					String name = null;
					if(items.hasItemMeta()){
						ItemMeta im = items.getItemMeta();
						if(im.hasDisplayName()){
							name = im.getDisplayName().replaceFirst("§r", "");
							name = name.replace("§", "")+i;
							
							config.set(name+".name", im.getDisplayName().replace('§', '&'));
						}
						if(im.hasLore()){
							config.set(name+".lore", convertLore(im));
						}
						if(im.hasEnchants()){
							config.set(name+".enchantment", convertEnchant(im));
						}
					 if(name == null)name = items.getType().name().toLowerCase()+i;
						config.set(name+".material", items.getType().name().toLowerCase());
						config.set(name+".id", items.getDurability());
						config.set(name+".amount", items.getAmount());
							inv = removeLine(i, config, inv);
						inv.add(i+":"+name);
					}else {
						if(removeLine(i, config, inv) != null){
							inv = removeLine(i, config, inv);
						}
						inv.add(i+":"+items.getType().name()+":"+items.getAmount()+":"+items.getDurability());
					}
				}
			}
		}
		config.set("items", inv);
		m.saveCustomConfig();
		m.reloadCustomConfig();
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
		int amont = config.getInt(name+"amount");
		im = new ItemStack(Material.getMaterial(materialName.toUpperCase()));
		im.setAmount(amont);
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
	public static Boolean hasItemMeta(int number, List<String> inv){
		String[] test = inv.get(number).split(":");
		if(test.length <= 2)return true;
		return false;
	}
	public static ItemStack getItemWithoutMeta(int i, FileConfiguration config, List<String> inv){
		ItemStack im = null;
		String[] is = inv.get(i).split(":");
		int amont = Integer.parseInt(is[2]);
		short id = (short)Short.parseShort(is[3]);
		im = new ItemStack(Material.getMaterial(is[1].toUpperCase()));
		im.setDurability(id);
		im.setAmount(amont);
		return im;
	}
	public static List<String> removeLine(int number, FileConfiguration config,List<String> inv){
		inv = config.getStringList("items");
		for(String str : inv){
			String[] m = str.split(":");
			if(m[0].equalsIgnoreCase(Integer.toString(number))){
				for(String string : config.getKeys(false)){
					if(string.replaceAll("[0-9]", "").equalsIgnoreCase(str)){
						config.set(string, null);
					}
				}
				inv.remove(str);
				return inv;
			}
		}
		return inv;
	}
	public static ArrayList<String> getInventoryList(){
		File f = new File(plugin.getDataFolder()+File.separator+"inventories"+File.separator);
		ArrayList<File> fileName = new ArrayList<File>(Arrays.asList(f.listFiles()));
		ArrayList<String> finalArray = new ArrayList<String>();
		for(File name : fileName){
			finalArray.add(name.getName().replace(".yml", ""));
		}
		return finalArray;
	}
}
