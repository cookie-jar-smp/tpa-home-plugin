package net.pifermet.spigotmavenplugin;

import net.pifermet.spigotmavenplugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class SpigotMavenPlugin extends JavaPlugin {

    public static SpigotMavenPlugin instance;

    public static SpigotMavenPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Recipes recipes = new Recipes();
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getCommand("hi").setExecutor(new HelloCommand());
        this.getCommand("tpa").setExecutor(new TeleportRequestCommand());
        this.getCommand("tpaccept").setExecutor(new TeleportAcceptCommand());
        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("delhome").setExecutor(new DelHomeCommand());
        this.getCommand("home").setExecutor(new GoToHomeCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("server stopped! -pifermet");
    }
}
