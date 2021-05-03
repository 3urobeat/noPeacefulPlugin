package org.herreurobeat.noPeacefulPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	String supportedversion = "1.16";
	String pluginversion = "1.0";
	
	@Override
	public void onEnable() {
		System.out.println("noPeacefulPlugin v" + pluginversion + " for " + supportedversion + " enabled!");
		
		Bukkit.getPluginManager().registerEvents(new Listeners(), this); //register the event listener
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //handle calling command methods

		//Quick response to info cmd
		if (cmd.getName().equalsIgnoreCase("npinfo")) {
			if (sender instanceof Player) { //check if sender is a player
				Player p = (Player) sender; //convert to player to be able to check current gamerules
				if(!p.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) return false; //check gamerule and stop if send command feedback is false
			}
			
			sender.sendMessage("noPeacefulPlugin by 3urobeat v" + pluginversion + " \n----\nBlocks users from running '/difficulty peaceful' to prevent mobs from despawning.\n----\nhttps://github.com/HerrEurobeat/noPeacefulPlugin");
		}
		
		return false;
	}
}

class Listeners implements Listener {	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent event) { //catch any command run by a player
		if (event.getMessage().equalsIgnoreCase("/difficulty peaceful")) {
			Player p = event.getPlayer(); //get the player who ran this command
			
			event.setCancelled(true); //prevent command from running
			
			if (!p.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) return; //check gamerule and stop if send command feedback is false
			p.sendMessage("[noPeacefulPlugin] Please choose a different difficulty. Peaceful is blocked because it will despawn mobs and can destroy farms.");
			System.out.println("[noPeacefulPlugin] Denied request to change difficulty to peaceful.");
			
			return;
		}
	}
	
	@EventHandler
	public void on(ServerCommandEvent event) { //catch any command run by the console
		if (event.getCommand().equalsIgnoreCase("difficulty peaceful")) { //console commands don't have a slash prefix
			event.setCancelled(true);
			
			event.getSender().sendMessage("[noPeacefulPlugin] Please choose a different difficulty. Peaceful is blocked because it will despawn mobs and can destroy farms."); //sending message to sender will also appear in Command Block output
			return;
		}
	}
}