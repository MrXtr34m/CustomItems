package fr.mrxtr34m.customitem;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	private CommandExecutor executor;
	private TabComplete tabComplete;
	public HashMap<String, org.bukkit.inventory.ItemStack> items = new HashMap<>();
	@Override
	public void onEnable() {
		Utils.setMain(this);
		executor = new CommandExecutor(this);
		tabComplete = new TabComplete();
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getCommand("customitems").setExecutor(executor);
		getCommand("customitems").setTabCompleter(tabComplete);
		setList();
		saveDefaultConfig();
		Utils.log(Level.INFO, "Has succesfully enabled");
	}
	@Override
	public void onDisable() {
		Utils.log(Level.INFO, "Has succesfully disabled");
	}
	public HashMap<String, org.bukkit.inventory.ItemStack> setList(){
		items.clear();
		for(String s :Utils.getItemsList()){
			ItemStack lel = new ItemStack(this, s);
			org.bukkit.inventory.ItemStack item = lel.getItemStack();
			items.put(s, item);
		}
		Utils.setItems(items);
		return items;
	}
}
