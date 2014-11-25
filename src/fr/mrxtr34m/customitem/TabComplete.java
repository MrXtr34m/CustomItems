package fr.mrxtr34m.customitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import fr.mrxtr34m.customitem.utils.Utils;

public class TabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("customitems")) {
			Player p = (Player) sender;
			if (args.length == 1) {
				ArrayList<String> valid = new ArrayList<>();
				List<String> array = Arrays.asList("current","give","remove","list","info");
				if(!args[0].equals("")){
					for(String value : array){
						if(value.startsWith(args[0].toLowerCase())){
							valid.add(value);
						}
					}
				}else {
					for(String value : array){
						valid.add(value);
					}
				}
				Collections.sort(valid);
				return valid;
			}
			if (args[0].equalsIgnoreCase("current")) {
				if (args.length == 2){
					List<String> array = Arrays.asList("name", "addLore", "addEnchant","addConfig", "info");
					ArrayList<String> arg = new ArrayList<>();
					if(args[1].equals("")){
						for(String value : array){
							arg.add(value);
						}
					}else{
						for(String value : array){
							if(value.toLowerCase().startsWith(args[1].toLowerCase())){
								arg.add(value);
							}
						}
					}
					Collections.sort(arg);
					return arg;
				}
				if (args[1].equalsIgnoreCase("addlore")) {
					if (args.length == 3) {
						if (p.getItemInHand() != null
								&& p.getItemInHand().getItemMeta().hasLore()) {
							return Arrays.asList("next", p.getItemInHand()
									.getItemMeta().getLore().size()
									+ 1 + "");
						} else {
							return Arrays.asList("next", "1");
						}
					}
				} else if (args[1].equalsIgnoreCase("addenchant")
						|| args[1].equalsIgnoreCase("addenchantement")) {
						if (args.length == 3) {
						ArrayList<String> enchat = new ArrayList<String>();
						if(!args[2].equals("")){
						for(Enchantment enchant : Enchantment.values()){
							if(enchant.getName().toLowerCase().startsWith(args[2].toLowerCase())){
								enchat.add(enchant.getName());
							}
						}
						}else {
							for(Enchantment enchant : Enchantment.values()){
									enchat.add(enchant.getName()); 
							}
						}
						Collections.sort(enchat);
						return enchat;
					}
					if(args.length == 4){
						return Arrays.asList("1");
					}
				}
			}else if (args[0].equalsIgnoreCase("give")) {
				if(args.length == 2){
					return items(args[1]);
				}
				
			}else if (args[0].equalsIgnoreCase("remove")) {
				if(args.length == 2){
					return items(args[1]);
				}
			}else if (args[0].equalsIgnoreCase("info")) {
				if(args.length == 2){
					return items(args[1]);
				}
			}
		}
		return null;
	}
	public ArrayList<String> items(String string){
		ArrayList<String> validItems = new ArrayList<>();
		List<String> available = Utils.getItemsList();
		if(!string.equals("")){
			for(String item : available){
				if(item.toLowerCase().startsWith(string)){
					validItems.add(item);
				}
			}
		}else {
			for(String item : available){
				validItems.add(item);
			}
		}
		Collections.sort(validItems);
		return validItems;
	}
}
