package retsrif.Blacksmith;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.nijiko.coelho.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

public class BPluginListener extends ServerListener {
	private final Blacksmith plugin;
	public static iConomy iconomy;
	public static BOSEconomy bose;
	String economy = Blacksmith.economy;
	
	public BPluginListener(Blacksmith instance) {
		plugin = instance;
	}
	
	public void onPluginEnable(PluginEnableEvent event) {
		 if(event.getPlugin().getDescription().getName().equals("iConomy") && economy.equalsIgnoreCase("iconomy")) {
	        iconomy = (iConomy)event.getPlugin();
	        System.out.println("[Blacksmith] attached to iConomy.");
		 }
		 
		 if(event.getPlugin().getDescription().getName().equals("BOSEconomy") && economy.equalsIgnoreCase("boseconomy")) {
			 bose = (BOSEconomy)event.getPlugin();
			 System.out.println("[Blacksmith] attached to BOSEconomy");
		 }
	}
	
	public void onPluginDisable(PluginDisableEvent event) {
		if(event.getPlugin().getDescription().getName().equals("iConomy") && economy.equalsIgnoreCase("iconomy")) {
            iconomy = null;
            System.out.println("[Blacksmith] lost connection to iConomy.");
    	}
		
		if(event.getPlugin().getDescription().getName().equals("BOSEconomy") && economy.equalsIgnoreCase("boseconomy")) {
			bose = null;
			System.out.println("[Blacksmith] lost connection to BOSEconomy.");
		}
	}
}
