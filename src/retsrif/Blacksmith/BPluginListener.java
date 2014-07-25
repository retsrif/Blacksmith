package retsrif.Blacksmith;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

//iConomy4
//import com.nijiko.coelho.iConomy.iConomy;
//iConomy5
import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

public class BPluginListener extends ServerListener {
	private final Blacksmith plugin;
	public static iConomy iconomy;
	public static BOSEconomy bose;
	
	public BPluginListener(Blacksmith instance) {
		plugin = instance;
	}
	
	public void onPluginEnable(PluginEnableEvent event)
        {
            if(event.getPlugin().getDescription().getName().equals("iConomy") && plugin.getIconomyState())
            {
                iconomy = (iConomy)event.getPlugin();
                System.out.println("[Blacksmith] attached to iConomy.");
            }
            else if(event.getPlugin().getDescription().getName().equals("BOSEconomy") && plugin.getBoseconomyState())
            {
                bose = (BOSEconomy)event.getPlugin();
		System.out.println("[Blacksmith] attached to BOSEconomy");
            }
	}
	
	public void onPluginDisable(PluginDisableEvent event)
        {
            if(event.getPlugin().getDescription().getName().equals("iConomy") && plugin.getIconomyState())
            {
            iconomy = null;
            System.out.println("[Blacksmith] lost connection to iConomy.");
            }
            else if(event.getPlugin().getDescription().getName().equals("BOSEconomy") && plugin.getBoseconomyState())
            {
                bose = null;
		System.out.println("[Blacksmith] lost connection to BOSEconomy.");
            }
	}
}