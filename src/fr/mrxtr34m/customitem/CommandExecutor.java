package fr.mrxtr34m.customitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
	
	public Main plugin;
		public CommandExecutor(Main plugin) {
			this.plugin = plugin;
}
	
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,
			String[] args) {
		if(!(s instanceof ConsoleCommandSender)){
			Player p = (Player) s;
			if(args.length == 0){
				Utils.sendGlobalHelp(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("current")){
				if(args.length == 1){
					Utils.sendGlobalHelp(p);
					return true;
				}
				if(args[1].equals("name")){
					ItemStack current = p.getItemInHand();
					if(!Utils.isItemStackIsValid(current)){
						Utils.sendError(p, "There is no item in your hand !");
						return true;
					}
					ItemMeta currentItemMeta = current.getItemMeta();
					short durability = current.getDurability();
					String name = null;
					for (int i = 2; i < args.length; i++) {
						if(args.length == 2){
							
						}
						else if(name == null){
							name = "§r"+args[i].replace('&', '§');
						}else 
							name = name +" "+args[i].replace('&', '§');
					}
					
					Utils.sendMsg(p, "You have set name to §r"+name);
					currentItemMeta.setDisplayName(name);
					current.setItemMeta(currentItemMeta);
					p.getInventory().remove(p.getItemInHand());
					current.setDurability(durability);
					p.setItemInHand(current);
					return true;
				}else if (args[1].equalsIgnoreCase("addenchant") ||args[1].equalsIgnoreCase("addenchantement") ) {
					if(args.length == 2 || args.length >= 5){
						Utils.sendError(p, "§9/ci current addenchant <enchant> <level>");
						return true;
					}
					ItemStack current = p.getItemInHand();
					if(!Utils.isItemStackIsValid(current)){
						Utils.sendError(p, "There is no item in your hand !");
						return true;
					}
						Enchantment enchant = null;
					try {
						enchant = Enchantment.getByName(args[2].toUpperCase());
						if(enchant == null){
							Utils.sendError(p, "This is not a valid enchantement");
							return true;
						}
					} catch (Exception e) {
						Utils.log(Level.WARNING, "Problem while trying to convert enchantement, report the folowings lines to the developer ASAP");
						e.printStackTrace();
						Utils.log(Level.WARNING, "Problem while trying to convert enchantement, report the folowings lines to the developer ASAP");
						Utils.sendError(p, "This is not a valid enchantement");
						return true;
					}
					Integer level;
					try {
						 level = Integer.parseInt(args[3]);
						 if(level >= 32768){
							 Utils.sendError(p, "This number is too hight (32767 max)");
							 return true;
						 }
					} catch (Exception e) {
						Utils.sendError(p, "This is not a valid number");
						return true;
					}
					ItemMeta cm = current.getItemMeta();
					cm.addEnchant(enchant, level, true);
					current.setItemMeta(cm);
					p.getInventory().removeItem(p.getItemInHand());
					p.getInventory().setItemInHand(current);
					 return true;
				}else if (args[1].equalsIgnoreCase("addlore")) {
					if(args.length == 3 || args.length == 2){
						Utils.sendAddLore(p);
						return true;
				}
					ItemStack c = p.getItemInHand();
					if(!Utils.isItemStackIsValid(c)){
						Utils.sendError(p, "There is no item in your hand !");
						return true;
					}
					int line = 0;
					String text = null;
					Boolean isNext = false;
					try {
						for (int i = 3; i < args.length; i++) {
							 if(text == null){
								text = args[i].replace('&', '§');
							}else 
								text = text +" "+args[i].replace('&', '§');
						}
					} catch (Exception e) {
						Utils.sendMsg(p, "This is not a valid text");
						return true;
					}
					if(args[2].equalsIgnoreCase("next")){
						isNext = true;
					}else {
						try {
						line = Integer.parseInt(args[2])-1;
						
						} catch (Exception e) {
							Utils.sendError(p, "This is not a valid number");
							return true;
						}	
					}
					
					ItemMeta cm = c.getItemMeta();
					short durability = c.getDurability();
					if(cm.hasLore() && !isNext){
						ArrayList<String> lore = (ArrayList<String>) cm.getLore();
						if(lore.size() < line || line <= 0){
							Utils.sendError(p, "The line number can't be set (size of lore =§7"+lore.size()+"§c)");
							return true;
						}try {
							String m = lore.get(line);
						} catch (IndexOutOfBoundsException e) {
							lore.add(text);
							cm.setLore(lore);
							c.setDurability(durability);
							c.setItemMeta(cm);
							p.getInventory().removeItem(p.getItemInHand());
							p.setItemInHand(c);
							return true;
							}
						String m = lore.get(line);
						if(m == null){
							lore.add(text);
						}else {
							lore.set(line,text);
						}
						cm.setLore(lore);
					}else if (isNext && cm.hasLore()) {
						ArrayList<String> lore = (ArrayList<String>) cm.getLore();
						lore.add(text);
						cm.setLore(lore);
					}
					else{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(text);
						cm.setLore(lore);
					}
					c.setDurability(durability);
					c.setItemMeta(cm);
					p.getInventory().removeItem(p.getItemInHand());
					p.setItemInHand(c);
					return true;
				}
				else if (args[1].equalsIgnoreCase("addConfig")) {
					if(args.length <= 2 || args[2].equalsIgnoreCase("config")){
						Utils.sendError(p, "You must specify a name");
						return true;
					}
					if(!Utils.isItemStackIsValid(p.getItemInHand())){
						Utils.sendError(p, "There is no item in your hand !");
						return true;
					}
					String name = args[2];
					Utils.saveItem(name, p.getItemInHand(), p);
					plugin.setList();
					return true;
					
				}
			}else if (args[0].equalsIgnoreCase("give")) {
				if(args.length <= 1 ||! Utils.getItemsList().contains(args[1])){
					Utils.sendError(p, "You must specify a valid name");
					return true;
				}
				plugin.setList();
				ItemStack item = Utils.getItemStack(args[1]);
				p.getInventory().addItem(item);
				String name = item.getItemMeta().getDisplayName();
				if(name != null)
				Utils.sendMsg(p, "The item "+item.getItemMeta().getDisplayName()+" §6has been given");
				else 
					Utils.sendMsg(p, "The item "+args[1]+"§6 has been given");
				return true;
			}else if (args[0].equalsIgnoreCase("remove")) {
				if(args.length <= 1 || !Utils.getItemsList().contains(args[1])){
					Utils.sendError(p, "You must specify a valid name");
					return true;
				}
				Utils.removeItem(args[1]);
				Utils.sendMsg(p, "The item "+args[1]+" has been deleted");
				return true;
			}else if (args[0].equalsIgnoreCase("list")) {
				String finalString = null;
				List<String> list = Utils.getItemsList();
				if(list.isEmpty()){
					Utils.sendError(p, "They are no items avec in your config file");
					return true;
				}
				for (int i = 0; i < list.size(); i++) {
					if(finalString == null){
						finalString = "§r"+list.get(i)+",";
					}else {
						if(i == list.size()){
							finalString = finalString+list.get(i);
						}else
						finalString = finalString+list.get(i)+",";
					}
				}
				Utils.sendMsg(p, finalString);
				return true;
			}else {
				Utils.sendGlobalHelp(p);
				return true;
			}
		}
		return false;
	}

}
