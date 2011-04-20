package retsrif.Blacksmith;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

import cosine.boseconomy.BOSEconomy;

public class BPlayerListener extends PlayerListener {
	public static Blacksmith plugin;
	boolean useIC = false;
	boolean useBOSE = false;
	boolean useMat = true;
	iConomy iconomy;
	BOSEconomy bose;
	
	public BPlayerListener(Blacksmith instance) {
		plugin = instance;
                useIC = plugin.getIconomyState();
                useBOSE = plugin.getBoseconomyState();
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(iconomy == null && bose == null) {
			setupEcon();
		}
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null) {
			return;
		}
		BlockState state = block.getState();
		ItemStack item = event.getItem();
		if(item == null) {
			return;
		}
		
		if(!(state instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign)state;
		
		if(hasCorrectFormat(sign) == false) {
			return;
		}
	
		int cost = getTotalCost(item);
			
		if(getSignType(sign).equalsIgnoreCase("Value")) {
			if(item.getDurability() == 0) {
				player.sendMessage(ChatColor.GREEN + "Tool at full durability.");
				return;
			}
			if(useIC || useBOSE) {
				if(cost<0) {
					player.sendMessage(ChatColor.RED + "Item not a tool.");
					return;
				}
			
				player.sendMessage(ChatColor.GOLD + "It will cost " + cost + " to repair your tool.");
			}
			else if(useMat) {
				String mat = getToolMaterial(item);
				sendMaterialMessages(player, mat);
			}
		}
		
		if(getSignType(sign).equalsIgnoreCase("Repair")) {
			if(item.getDurability() == 0) {
				player.sendMessage(ChatColor.GREEN + "Tool at full durability.");
				return;
			}
			if(useIC || useBOSE) {
				if(cost<0) {
					player.sendMessage(ChatColor.RED + "Item not a tool.");
					return;
				}
			
				if(!isBalanceGreaterThanCost(player, cost)) {
					player.sendMessage(ChatColor.RED + "Not enough money to repair.");
					return;
				}
				fixItem(item, player);
				subtractMoney(player, cost);
			}
			else if(useMat) {
				String mat = getToolMaterial(item);
				Material mate = changeToMat(mat, player);
				if(!(checkPlayerInv(player, mate))) {
					sendNoMessages(player, mat);
					return;
				}
				removeMat(player, mate, item);
				player.sendMessage(ChatColor.GREEN + "Tool repaired!");
			}

		}
		
	}
	
	public boolean hasCorrectFormat(Sign sign) {
		String first = sign.getLine(0);
		String second = sign.getLine(1);
		
		if(first.equalsIgnoreCase("[Blacksmith]") && second.equalsIgnoreCase("Value")) {
			return true;
		}
		else if(first.equalsIgnoreCase("[Blacksmith]") && second.equalsIgnoreCase("Repair")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getSignType(Sign sign) {
		String line = sign.getLine(1);
		
		if(line.equalsIgnoreCase("Value")) {
			return line;
		}
		
		else if(line.equalsIgnoreCase("Repair")) {
			return line;
		}
		
		else {
			return null;
		}
	}
	
	public int getTotalCost(ItemStack tool) {
		Material type = tool.getType();
		int max = type.getMaxDurability();
		int durability = tool.getDurability();
		double ratio = (double)durability/max;
		double cost = 0;
		String tm = getToolMaterial(tool);
		
		if(tm.equalsIgnoreCase("wood")) {
			cost = Blacksmith.woodBase*ratio;
			return (int)cost;
		}
		
		if(tm.equalsIgnoreCase("stone")) {
			cost = Blacksmith.stoneBase*ratio;
			return (int)cost;
		}
		
		if(tm.equalsIgnoreCase("iron")) {
			cost = Blacksmith.ironBase*ratio;
			return (int)cost;
		}

		if(tm.equalsIgnoreCase("gold")) {
			cost = Blacksmith.goldBase*ratio;
			return (int)cost;
		}

		if(tm.equalsIgnoreCase("diamond")) {
			cost = Blacksmith.diamondBase*ratio;
			return (int)cost;
		}
		else {
			return -1;
		}
	}
	
	public String getToolMaterial(ItemStack tool) {
		Material type = tool.getType();
		
		if(type == Material.WOOD_AXE || type == Material.WOOD_HOE || 
				type == Material.WOOD_PICKAXE || type == Material.WOOD_SPADE || 
				type == Material.WOOD_SWORD) {
			return "wood";
		}
		
		else if(type == Material.STONE_AXE || type == Material.STONE_HOE || 
				type == Material.STONE_PICKAXE || type == Material.STONE_SPADE || 
				type == Material.STONE_SWORD) {
			return "stone";
		}
		
		else if(type == Material.IRON_AXE || type == Material.IRON_HOE || 
				type == Material.IRON_PICKAXE || type == Material.IRON_SPADE || 
				type == Material.IRON_SWORD) {
			return "iron";
		}
		
		else if(type == Material.GOLD_AXE || type == Material.GOLD_HOE || 
				type == Material.GOLD_PICKAXE || type == Material.GOLD_SPADE || 
				type == Material.GOLD_SWORD) {
			return "gold";
		}
		
		else if(type == Material.DIAMOND_AXE || type == Material.DIAMOND_HOE || 
				type == Material.DIAMOND_PICKAXE || type == Material.DIAMOND_SPADE || 
				type == Material.DIAMOND_SWORD) {
			return "diamond";
		}
		else {
			return "";
		}
	}
	
	public boolean isBalanceGreaterThanCost(Player player, int cost) {
		String playerName = player.getName();
		
		if(useIC) {
			double balance = iconomy.getBank().getAccount(playerName).getBalance();
			
			if(balance>cost)
				return true;
			else 
				return false;
		}
		else if(useBOSE) {
			double balance = bose.getPlayerMoney(playerName);
			
			if(balance>cost)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	public void subtractMoney(Player player, int cost) {
		String playerName = player.getName();
		if(useIC) {
			Account account = iconomy.getBank().getAccount(playerName);
			double balance = account.getBalance();
			account.setBalance(balance-cost);
		}
		else if(useBOSE) {
			double balance = bose.getPlayerMoney(playerName);
			bose.setPlayerMoney(playerName, (int)balance - cost, true);
		}
	}
	
	public void setupEcon() {
		if(useIC) {
			iconomy = BPluginListener.iconomy;
			useMat = false;
		}
        else if(useBOSE) {
        	bose = BPluginListener.bose;
        	useMat = false;
        }
	}
	
	public void sendMaterialMessages(Player player, String mat) {
		if(mat.equalsIgnoreCase("wood")) {
			player.sendMessage(ChatColor.GOLD + "It will cost one plank to repair your tool.");
		}
		else if(mat.equalsIgnoreCase("stone")) {
			player.sendMessage(ChatColor.GOLD + "It will cost one stone to repair your tool.");
		}
		else if(mat.equalsIgnoreCase("diamond")) {
			player.sendMessage(ChatColor.GOLD + "It will cost one diamond to repair your tool.");
		}
		else if(!(mat.equals(""))) {
			player.sendMessage(ChatColor.GOLD + "It will cost one " + mat + " ingot to repair your tool.");
		}
	}
	
	public boolean checkPlayerInv(Player player, Material mat) {
		ItemStack[] inv = player.getInventory().getContents();
		for(ItemStack is : inv) {
			if(is != null && is.getType() == mat) {
				return true;
			}
		}
		return false;
	}
	
	public void sendNoMessages(Player player, String mat) {
		if(mat.equalsIgnoreCase("wood")) {
			player.sendMessage(ChatColor.RED + "You need a plank to fix this.");
		}
		else if(mat.equalsIgnoreCase("stone")) {
			player.sendMessage(ChatColor.RED + "You need cobblestone to fix this.");
		}
		else if(mat.equalsIgnoreCase("diamond")) {
			player.sendMessage(ChatColor.RED + "You need a diamond to fix this.");
		}
		else if(!(mat.equals(""))) {
			player.sendMessage(ChatColor.GOLD + "You need a " + mat + " ingot to fix this.");
		}
	}
	
	public void removeMat(Player player, Material mat, ItemStack item) {
		//thanks to mcMMO for part of this
		ItemStack[] inv = player.getInventory().getContents();
		ItemStack tool = player.getItemInHand();
		for(ItemStack is : inv) {
			if(is.getType() == mat) {
				if(is.getAmount() == 1) {
					is.setTypeId(0);
					is.setAmount(0);
					short dur = 0;
					tool.setDurability(dur);
					player.getInventory().setContents(inv);
				}
				else {
					is.setAmount(is.getAmount() - 1);
					short dur = 0;
					tool.setDurability(dur);
					player.getInventory().setContents(inv);
				}
				return;
			}
		}
	}
	
	public Material changeToMat(String mat, Player player) {
		if(mat.equals("wood")) {
			return Material.WOOD;
		}
		
		else if(mat.equals("stone")) {
			return Material.COBBLESTONE;
		}
		
		else if(mat.equals("iron")) {
			return Material.IRON_INGOT;
		}
		
		else if(mat.equals("gold")) {
			return Material.GOLD_INGOT;
		}
		
		else if(mat.equals("diamond")) {
			return Material.DIAMOND;
		}
		else {
			return Material.AIR;
		}
	}
	
	public void fixItem(ItemStack item, Player player) {
		short newDur = 0;
		item.setDurability(newDur);
		player.sendMessage(ChatColor.GREEN + "Tool repaired!");
	}
}