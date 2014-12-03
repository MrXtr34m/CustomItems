package fr.mrxtr34m.customitem;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mrxtr34m.customitem.file.ConfigFileUtils;
import fr.mrxtr34m.customitem.file.ConfigfileManager;
import fr.mrxtr34m.customitem.utils.ConfigUtils;
import fr.mrxtr34m.customitem.utils.Utils;

public class Main extends JavaPlugin{
	private CommandExecutor executor;
	private TabComplete tabComplete;
	public HashMap<String, ItemStack> items = new HashMap<>();
	public ConfigUtils cfg;
	public ConfigfileManager manager;
	@Override
	public void onEnable() {
		ConfigFileUtils.setMain(this);
		Utils.setMain(this);
		executor = new CommandExecutor(this);
		tabComplete = new TabComplete();
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getCommand("customitems").setExecutor(executor);
		getCommand("customitems").setTabCompleter(tabComplete);
		cfg = new ConfigUtils(this);
		setList();
		saveDefaultConfig();
		Utils.log(Level.INFO, "Has succesfully enabled");
		manager = new ConfigfileManager(this);
	}
	@Override
	public void onDisable() {
		Utils.log(Level.INFO, "Has succesfully disabled");
	}
	public HashMap<String, ItemStack> setList(){
		items.clear();
		for(String s :Utils.getItemsList()){
			ItemStack item = ConfigUtils.getItemStack(s, getConfig());
			items.put(s, item);
		}
		Utils.setItems(items);
		return items;
	}
	public ConfigfileManager getFileConfigManager(){
		return manager;
	}
	
}
