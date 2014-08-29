package retsrif.Blacksmith;
import java.io.File;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

public class Blacksmith extends JavaPlugin {
	public BPlayerListener bpl;
	public BPluginListener bplugl;
	public static int minCost = 1;
	public static int woodBase = 20;
	public static int stoneBase = 50;
	public static int ironBase = 100;
	public static int goldBase = 75;
	public static int diamondBase = 200;
	public static boolean bIconomy = false;
        public static boolean bBoseconomy = false;
	Configuration config;
	
	@Override
	public void onDisable() {
		System.out.println("[Blacksmith] enabled.");
	}

	@Override
	public void onEnable() {
                config = getConfiguration();
		if(!(new File(getDataFolder(), "config.yml")).exists()) {
			defaultConfig();
		}
		loadConfig();

                bpl = new BPlayerListener(this);
                bplugl = new BPluginListener(this);
                
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, bpl, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, bplugl, Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLUGIN_DISABLE, bplugl, Priority.Monitor, this);


		if(bIconomy) {
			Plugin ic = pm.getPlugin("iConomy");
			if(ic != null) {
				if(ic.isEnabled()) {
					iConomy iconomy = (iConomy)ic;
					BPluginListener.iconomy = iconomy;
					System.out.println("[Blacksmith] has found iConomy.");
				}
			}
		}
		else if(bBoseconomy) {
			Plugin boseconomy = pm.getPlugin("BOSEconomy");
			if(boseconomy != null) {
				if(boseconomy.isEnabled()) {
					BOSEconomy bose = (BOSEconomy)boseconomy;
					BPluginListener.bose = bose;
					System.out.println("[Blacksmith] has found BOSEconomy");
				}
			}
		}
		
		System.out.println("[Blacksmith] enabled.");
	}
	
	private void loadConfig() {
		config.load();
		minCost = config.getInt("minimum-cost", 1);
		woodBase = config.getInt("wood-base", 20);
		stoneBase = config.getInt("stone-base", 50);
		ironBase = config.getInt("iron-base", 100);
		goldBase = config.getInt("gold-base", 75);
		diamondBase = config.getInt("diamond-base", 200);
                bIconomy = config.getBoolean("use-iconomy", false);
                bBoseconomy = config.getBoolean("use-boseconomy", false);
	}
	
	private void defaultConfig() {
		config.setProperty("minimum-cost", 1);
		config.setProperty("wood-base", 20);
		config.setProperty("stone-base", 50);
		config.setProperty("iron-base", 100);
		config.setProperty("gold-base", 75);
		config.setProperty("diamond-base", 200);
        config.setProperty("use-iconomy", false);
        config.setProperty("use-boseconomy", false);
		config.save();
	}

        public boolean getIconomyState() { return bIconomy; }
        public boolean getBoseconomyState() { return bBoseconomy; }
}