package fr.mrxtr34m.customitem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrxtr34m.customitem.Main;

public class Utils {
	public static String prefix = "§6[§7CustomItems§6] ";
	public static Main plugin;
	public static HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();
	
	public static String centerText(String text) {
		int maxWidth = 80,
		spaces = (int) Math.round((maxWidth-1.4*ChatColor.stripColor(text).length())/2);
		return StringUtils.repeat(" ", spaces)+text;
	}
	public static void setMain(Main plugin){
		Utils.plugin = plugin;
	}
	public static boolean isItemStackIsValid(ItemStack item){
		if(item.getType() == Material.AIR || item.getType() == null)return false;
		return true;
	}
	public static void sendMsg(Player p, String m){
		p.sendMessage(prefix+"§6"+m);
	}
	public static void sendError(Player p, String m){
		p.sendMessage(prefix+"§c"+m);
	}
	public static String getPrefix(){
		return prefix;
	}
	public static void log(Level Level, String message){
		Bukkit.getLogger().log(Level, "[CustomsItems] "+message);
	}
	public static void sendGlobalHelp(Player p){
		p.sendMessage(Utils.centerText("§6------------§cCustom Items§c------------"));
		p.sendMessage("§9/ci current §c<name|addEnchant|addlore|addconfig>");
		p.sendMessage("§9/ci give §c<name>");
		p.sendMessage("§9/ci list");
		p.sendMessage(Utils.centerText("§6------------------------------------"));
	}
	public static void sendNameHelp(Player p){
		p.sendMessage("§9/ci current name §c<TheName> §o(Don't set a name to reset name)");
	}
	public static void sendAddEnchant(Player p){
		p.sendMessage("§9/ci current addEnchant §c<Enchant> <levels>");
	}
	public static void sendAddLore(Player p){
		p.sendMessage("§9/ci current setLore §c<lineNumber|next> <Text>");
	}
	public static List<String> getItemsList(){
		List<String> list = new ArrayList<>();
		for(String key : plugin.getConfig().getKeys(false)){
			if(key != "config"){
				list.add(key);
			}
		}
		return list;
		}
	public static void setItems(HashMap<String, ItemStack> lel){
	items =lel;
	}
	public static ItemStack getItemStack(String name){
		return items.get(name);
	}
	public static void removeItem(String path){
		plugin.getConfig().set(path, null);
		plugin.saveConfig();
		plugin.setList();
	}
	public static void getInformationForItemStack(ItemStack itemStack, Player p){
		ItemMeta im = itemStack.getItemMeta();
		p.sendMessage(centerText("§6-------------§9Informations§6-------------"));
		Utils.sendMsg(p, "Material: §7"+itemStack.getType().name());
		Utils.sendMsg(p, "Data:§7 "+itemStack.getData());
		if(im.hasDisplayName()){
		Utils.sendMsg(p, "Name: §7"+im.getDisplayName());
		}
		Utils.sendMsg(p, "Lore:");
		if(im.hasLore()){
		for(String lore : im.getLore()){
			Utils.sendMsg(p, lore);
		}
		}
		Utils.sendMsg(p, "Enchantements:");
		if(im.hasEnchants()){
		for(Entry<Enchantment, Integer> enchant : im.getEnchants().entrySet()){
			Utils.sendMsg(p, enchant.getKey().getName()+" "+enchant.getValue());
		}
		}
	}
	public static boolean isPlayerHasPermissions(Player p, String command){
		if(p.isOp() || p.hasPermission("customitems."+command))return true;
		permError(p);
		return false;
	}
	public static void permError(Player p){
		Utils.sendError(p, "Sorry you don't have enought permissions to do this !");
	}
}
