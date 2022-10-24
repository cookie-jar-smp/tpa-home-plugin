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

import java.time.LocalTime;
import java.util.Timer;

public class TeleportAcceptCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Boolean foundPlayer = false;
            PersistentDataContainer pdc = Bukkit.getPlayer(sender.getName()).getPersistentDataContainer();
            NamespacedKey latestRequestKey = new NamespacedKey(SpigotMavenPlugin.getInstance(), "latestTpRequest");
            String[] latestRequests = pdc.get(latestRequestKey, PersistentDataType.STRING).split(",");
            String endingString = "";
            for ( String request: latestRequests) {
                if (request.split(" ")[0].equals(args[0])) {
                    foundPlayer = true;
                    sender.sendMessage(ChatColor.GREEN + "You accepted the teleport request. ");
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GREEN + sender.getName() + " accepted your teleport request.");
                    sender.sendMessage(ChatColor.YELLOW  + "You will be teleported in 5 seconds. If you move/take damage this will be cancelled. ");
                    Location prevLoc = Bukkit.getPlayer(args[0]).getLocation();
                    Timer t = new java.util.Timer();
                    t.schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    if (prevLoc.equals(Bukkit.getPlayer(sender.getName()).getLocation())) {
                                        Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(sender.getName()));
                                        sender.sendMessage(ChatColor.GREEN + "You did not move so you have been teleported! :D");
                                    } else {
                                        sender.sendMessage(ChatColor.YELLOW + "Uh oh! You moved so you were not teleported! :(");
                                    }
                                }
                            },
                            5000);
                    sender.sendMessage(ChatColor.GREEN + "You have been teleport to home #" + args[0] + ".");
                    return true;
                } else if (!request.equals("")){
                    endingString = endingString + "," + request;
                }
            }
            if (!foundPlayer) {
                throw new Exception("Player is invalid");
            } else {
                pdc.set(latestRequestKey, PersistentDataType.STRING, endingString);
            }
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.YELLOW + "The teleport command you have tried to accept is either invalid or expired. Please try again." + e.toString());
            return true;
        }
    }
}
