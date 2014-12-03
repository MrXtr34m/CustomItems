package fr.mrxtr34m.customitem.file;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import fr.mrxtr34m.customitem.Main;

public class ConfigfileManager {
	public ArrayList<ConfigFile> config = new ArrayList<ConfigFile>();
	public List<String> configName;
	public Main plugin;
	
	public ConfigfileManager(Main plugin) {
		this.plugin = plugin;
		addConfig();
	}
	public void addConfig(){
		configName = ConfigFileUtils.getInventoryList();
		for(String string :configName){
			Bukkit.getLogger().info("Hooked inventory: "+string);
			ConfigFile config = new ConfigFile(string, plugin);
			this.config.add(config);
		}
	}
	public ConfigFile getConfig(String name){
		for(ConfigFile config : config){
			if(config.getName().equalsIgnoreCase(name))
				return config;
		}
		return null;
	}
	public boolean isValidConfig(String name){
		return configName.contains(name);
	}
}
