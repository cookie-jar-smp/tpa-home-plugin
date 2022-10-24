package net.pifermet.spigotmavenplugin.commands;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
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

public class DelHomeCommand implements CommandExecutor {
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
            if (!pdc.has(homeKey, PersistentDataType.INTEGER_ARRAY)) {
                sender.sendMessage(ChatColor.YELLOW + "You do not have a home there to delete!");
                sentErrorMessage = true;
                throw new Exception("Player does not have a home");
            } else {
                Location playerLocation = Bukkit.getPlayer(sender.getName()).getLocation();
                int[] pdcCoords = pdc.get(homeKey, PersistentDataType.INTEGER_ARRAY);
                pdc.remove(homeKey);
                sender.sendMessage(ChatColor.GREEN + "Home number #" + args[0] + " at " + pdcCoords[0] + ", " + pdcCoords[1] + ", " + pdcCoords[2] + " has been deleted.");
            }

            return true;
        } catch (Exception e) {
            if (!sentErrorMessage) {
                sender.sendMessage(ChatColor.RED + "An error has occurred please try entering the command again. " + e.toString());
            }
            return true;
        }
    }
}
