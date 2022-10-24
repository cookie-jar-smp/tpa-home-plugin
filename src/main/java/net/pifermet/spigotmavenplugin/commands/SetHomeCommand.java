package net.pifermet.spigotmavenplugin.commands;

import net.pifermet.spigotmavenplugin.SpigotMavenPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SetHomeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        boolean sentErrorMessage = false;
        try {
            PersistentDataContainer pdc = Bukkit.getPlayer(sender.getName()).getPersistentDataContainer();
            if (!args[0].equals("1") && !args[0].equals("2") && !args[0].equals("3")) {
                sentErrorMessage = true;
                sender.sendMessage(ChatColor.YELLOW + "The only accepted inputs are 1, 2, or 3.");
                throw new Exception("Input was not 1, 2, or 3.");
            }
            NamespacedKey homeKey = new NamespacedKey(SpigotMavenPlugin.getInstance(), "home" + args[0]);
            NamespacedKey homeWorldKey = new NamespacedKey(SpigotMavenPlugin.getInstance(), "homeWorld" + args[0]);
            if (pdc.has(homeKey, PersistentDataType.INTEGER_ARRAY)) {
                sender.sendMessage(ChatColor.YELLOW + "You have already set a home other! Use /delhome <homenumber> to delete that home. ");
                sentErrorMessage = true;
                throw new Exception("Player already has home");
            } else {
                Location playerLocation = Bukkit.getPlayer(sender.getName()).getLocation();
                pdc.set(homeKey, PersistentDataType.INTEGER_ARRAY, new int[]{playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ()});
                String worldType = "";
                if (playerLocation.getWorld().getName().equals("world")) {
                    worldType = "overworld";
                } else if (playerLocation.getWorld().getName().equals("world_the_nether")) {
                    worldType = "the_nether";
                } else if (playerLocation.getWorld().getName().equals("world_the_end")) {
                    worldType = "the_end";
                }
                pdc.set(homeWorldKey, PersistentDataType.STRING, worldType);
                sender.sendMessage(ChatColor.GREEN + "Home number #" + args[0] + " has been set.");
            }
            return true;
        } catch (Exception e) {
            if (!sentErrorMessage){
                sender.sendMessage(ChatColor.YELLOW + "An error has occured. Please re-enter the command. " + e.toString());
            }
            return true;
        }

    }
}
