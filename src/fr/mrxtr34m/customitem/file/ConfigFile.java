package fr.mrxtr34m.customitem.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mrxtr34m.customitem.Main;

public class ConfigFile {
	private String name;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private Main plugin;
	public ConfigFile(String name, Main plugin) {
	this.plugin = plugin;
	this.name = name;
	reloadCustomConfig();
	saveCustomConfig();
	}
	public void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    	File g =new File(plugin.getDataFolder()+File.separator+"inventories");
	    	if(!g.mkdir()){
	    		g.mkdir();
	    	}
	    customConfigFile = new File(plugin.getDataFolder()+ File.separator + "inventories",name+".yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	    InputStream defConfigStream = plugin.getResource(name+".yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}
	public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        reloadCustomConfig();
	    }
	    return customConfig;
	}
	public void saveCustomConfig() {
	    if (customConfig == null || customConfigFile == null) {
	        return;
	    }
	    try {
	        getCustomConfig().save(customConfigFile);
	        Bukkit.getLogger().log(Level.INFO, "Saved !");
	    } catch (IOException ex) {
	       Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	public String getName(){
		return name;
	}
}
