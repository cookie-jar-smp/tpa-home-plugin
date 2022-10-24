package net.pifermet.spigotmavenplugin.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pifermet.spigotmavenplugin.SpigotMavenPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;

public class TeleportRequestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player reciever = Bukkit.getPlayer(args[0]);
            if (reciever.getName().equals(sender.getName())) {
                throw new Exception("Player tried to tp to themselves. ");
            }
            PersistentDataContainer pdc = reciever.getPersistentDataContainer();
            NamespacedKey latestRequest = new NamespacedKey(SpigotMavenPlugin.getInstance(), "latestTpRequest");

            String requestInfo = pdc.get(latestRequest, PersistentDataType.STRING);
            if (requestInfo.split(",").length > 0) {
                for (String request: requestInfo.split(",")) {
                    if (request.split(" ")[0].equals(sender.getName())) {
                        sender.sendMessage(ChatColor.YELLOW + "You have already sent a teleport request to that player! Please wait for it to expire to send a new one.  ");
                        return true;
                    }
                }
            }
            Integer id = requestInfo.split(",").length;
            pdc.set(latestRequest, PersistentDataType.STRING, requestInfo + "," + sender.getName() + " " + id);
            TextComponent tc = new TextComponent(ChatColor.BLUE + sender.getName() + " wants to teleport to you. You have 30 seconds to accept. Run /tpaccept " + sender.getName() + " to accept.");
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender.getName()));
            reciever.spigot().sendMessage(tc);
            sender.sendMessage(ChatColor.BLUE + "You have sent a teleport request to " + reciever.getName() + ". They have 30 seconds to accept. ");

            Timer t = new java.util.Timer();
            t.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            String updatedRequests = pdc.get(latestRequest, PersistentDataType.STRING);
                            String newRequests = "";
                            for (String request : updatedRequests.split(",")) {
                                if (!request.equals("") && request.split(" ")[1].equals(id.toString())) {
                                    sender.sendMessage(ChatColor.YELLOW + "Your teleport request to " + args[0] + " has expired. ");
                                    reciever.sendMessage(ChatColor.YELLOW + "The teleport request from " + sender.getName() + " has expired. ");
                                } else if (!request.equals("")){
                                    newRequests = newRequests + "," + request;
                                }
                            }
                            pdc.set(latestRequest,PersistentDataType.STRING, newRequests);
                        }
                    },
                    30000
            );
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.YELLOW + "The player name or command you have entered is either invalid or already sent.  ");

            return true;
        }
    }
}
