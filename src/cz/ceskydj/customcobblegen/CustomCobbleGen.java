package cz.ceskydj.customcobblegen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.spaceio.customoregen.CustomOreGen;

public class CustomCobbleGen extends JavaPlugin {
    private CustomOreGen customOreGen;

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);

        if (!this.isEnabled()) {
            return;
        }

        // Compatibility options for CustomOreGen
        try {
            this.customOreGen = this.getCustomOreGen();

            this.getLogger().info("CustomOreGen has been found! Applying compatibility options...");

            this.configManager.addCompatibilityOptionsToCustomOreGen(this.customOreGen);

            this.getLogger().info("Compatibility options have been applied to CustomOreGen's config.");
        } catch (PluginNotFoundException ignored) {}

        this.getServer().getPluginManager().registerEvents(new BlockFromToListener(this), this);
        this.getCommand("ccgreload").setExecutor(new CCGReloadCommand(this));

        this.getLogger().info("Plugin loaded successfully");
    }

    private CustomOreGen getCustomOreGen() throws PluginNotFoundException {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("CustomOreGen");

        if (!(plugin instanceof  CustomOreGen)) {
            throw new PluginNotFoundException("Plugin CustomOreGen hasn't been found.");
        }

        return (CustomOreGen) plugin;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void dump(String message) {
        if (this.configManager.isDebugOn()) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[CustomCobbleGen] " + message);
        }
    }

    public Player getClosestPlayer(Location location) {
        // From https://gist.github.com/fourohfour/8243657
        double closestDistance = Double.MAX_VALUE;
        Player closestPlayer = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = player.getLocation().distance(location);
            if (closestDistance == Double.MAX_VALUE || distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }
}