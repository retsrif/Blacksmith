package retsrif.Blacksmith;
import java.io.File;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

//iConomy4
//import com.nijiko.coelho.iConomy.iConomy;
//iConomy5
import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

public class Blacksmith extends JavaPlugin {
	public BPlayerListener bpl;
	public BPluginListener bplugl;
	public static int woodBase = 50;
	public static int stoneBase = 75;
	public static int ironBase = 150;
	public static int goldBase = 250;
	public static int diamondBase = 500;
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
		woodBase = config.getInt("wood-base", 50);
		stoneBase = config.getInt("stone-base", 75);
		ironBase = config.getInt("iron-base", 150);
		goldBase = config.getInt("gold-base", 250);
		diamondBase = config.getInt("diamond-base", 500);
                bIconomy = config.getBoolean("use-iconomy", false);
                bBoseconomy = config.getBoolean("use-boseconomy", false);
	}
	
	private void defaultConfig() {
		config.setProperty("wood-base", 50);
		config.setProperty("stone-base", 75);
		config.setProperty("iron-base", 150);
		config.setProperty("gold-base", 250);
		config.setProperty("diamond-base", 500);
                config.setProperty("use-iconomy", false);
                config.setProperty("use-boseconomy", false);
		config.save();
	}

        public boolean getIconomyState() { return bIconomy; }
        public boolean getBoseconomyState() { return bBoseconomy; }
}