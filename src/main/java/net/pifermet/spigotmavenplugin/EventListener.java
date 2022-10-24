package net.pifermet.spigotmavenplugin;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class EventListener implements Listener {
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getPersistentDataContainer().set(new NamespacedKey(SpigotMavenPlugin.getInstance(), "latestTpRequest"), PersistentDataType.STRING, "");
        event.setJoinMessage(ChatColor.GREEN + event.getPlayer().getDisplayName() + " just joined. Welcome!");
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.YELLOW + event.getPlayer().getDisplayName() + " just quit. Goodbye!");
    }

    @EventHandler
    public void EntityHitEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            BlockData redstoneBlockData = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
            int amount = 16;
            double offset = 0.33;
            event.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation(), amount, offset, offset, offset, redstoneBlockData);
        }
    }
}
