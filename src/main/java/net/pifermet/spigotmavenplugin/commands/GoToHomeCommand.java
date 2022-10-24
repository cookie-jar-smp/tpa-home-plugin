package net.pifermet.spigotmavenplugin.commands;

import net.pifermet.spigotmavenplugin.SpigotMavenPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Timer;

import static org.bukkit.Bukkit.getServer;

public class GoToHomeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
            if (!pdc.has(homeKey, PersistentDataType.INTEGER_ARRAY)) {
                sender.sendMessage(ChatColor.YELLOW + "You do not have a home there to teleport to!");
                sentErrorMessage = true;
                throw new Exception("Player does not have a home");
            } else {
                int[] intcoords = pdc.get(homeKey, PersistentDataType.INTEGER_ARRAY);
                double coords[] = new double[3];
                coords[0] = intcoords[0];
                coords[1] = intcoords[1];
                coords[2] = intcoords[2];
                sender.sendMessage(ChatColor.YELLOW  + "You will be teleported in 5 seconds. If you move/take damage this will be cancelled. ");
                Location prevLoc = Bukkit.getPlayer(sender.getName()).getLocation();
                BukkitRunnable runnable = new BukkitRunnable() {
                    public void run() {
                        if (prevLoc.equals(Bukkit.getPlayer(sender.getName()).getLocation())) {
                            sender.sendMessage(ChatColor.GREEN + "You have been teleported to home #" + args[0] + ".");
                            String command = "execute in minecraft:" + pdc.get(homeWorldKey,PersistentDataType.STRING) + " run tp " + sender.getName() + " " + coords[0] + " " + coords[1] + " " + coords[2];
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),command);
                            sender.sendMessage(ChatColor.GREEN + "You did not move so you have been teleported! :D");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "Uh oh! You moved so you were not teleported! :(");
                        }
                    }
                };
                runnable.runTaskLater(SpigotMavenPlugin.getInstance(),100);
            }
            return true;
        } catch (Exception e) {
            if (!sentErrorMessage) {
                sender.sendMessage(ChatColor.RED + "An error has occurred please try entering your command again. " + e.toString());
            }
            return true;
        }
    }
}
