package cz.ceskydj.customcobblegen;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import xyz.spaceio.customoregen.CustomOreGen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigManager {
    private final CustomCobbleGen plugin;

    private List<Generator> generators;

    public ConfigManager(CustomCobbleGen customCobbleGen) {
        this.plugin = customCobbleGen;

        this.generators = new ArrayList<>();

        this.loadConfig();
    }

    private void loadConfig() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        if (!new File(this.plugin.getDataFolder(), "config.yml").exists()) {
            this.plugin.saveDefaultConfig();
        } else {
            this.reloadConfig();
        }
    }

    private void loadGenerators() {
        ConfigurationSection generatorsSection = this.plugin.getConfig().getConfigurationSection("generators");
        Map<String, Object> configRecords = generatorsSection.getValues(false);

        List<String> usedWorlds = new ArrayList<>();
        configRecords.forEach((name, configs) -> {
            if (name.contains(".")) {
                return;
            }

            ConfigurationSection generatorConfig = generatorsSection.getConfigurationSection(name);
            List<String> worlds = generatorConfig.getStringList("worlds");
            List<String> configBlocks = generatorConfig.getStringList("blocks");
            Map<Material, Double> blocks = new HashMap<>();

            worlds.forEach(world -> {
                if (this.plugin.getServer().getWorld(world) == null) {
                    this.configError("'" + world + "' is invalid world name.");

                    return;
                }

                if (usedWorlds.contains(world)) {
                    this.configError("One world can be used with one generator only. World '" + world + "' is in more generators.");

                    return;
                }

                usedWorlds.add(world);
            });

            AtomicReference<Double> chanceSum = new AtomicReference<>(0.0);
            configBlocks.forEach(configBlockRow -> {
                String[] configBlockParts = configBlockRow.split(":");
                Material material = Material.getMaterial(configBlockParts[0]);
                Double chance = Double.parseDouble(configBlockParts[1]);

                if (material == null) {
                    this.configError("You use bad material name '" + configBlockParts[0] + "' in '" + name + "' generator. Go to  https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html and find the good block name there.");

                    return;
                }

                if (chance <= 0) {
                    this.configError("chance of material '" + configBlockParts[0] + "' is lower than or equals to 0 in '" + name + "' generator.");

                    return;
                }

                if (chanceSum.updateAndGet(v -> v + chance) > 100.0) {
                    this.configError("chance is higher than 100 % in '" + name + "' generator.");

                    return;
                }

                blocks.put(material, chance);
            });

            this.generators.add(new Generator(name, worlds, blocks));
        });
    }

    private void configError(String message) {
        this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[CustomCobbleGen] Config error! " + message);
        this.plugin.getPluginLoader().disablePlugin(this.plugin);
    }

    public void reloadConfig() {
        this.plugin.reloadConfig();

        this.loadGenerators();
    }

    public boolean isDebugOn() {
        return this.plugin.getConfig().getBoolean("debug");
    }

    public String getMessage(String name) {
        return this.plugin.getConfig().getString("messages." + name);
    }

    public Generator getGenerator(String world) {
        for (Generator generator :
                this.generators) {
            if (generator.getWorlds().contains(world)) {
                return generator;
            }
        }

        return null;
    }

    public List<String> getUsedWorlds() {
        List<String> worlds = new ArrayList<>();

        this.generators.forEach(generator -> worlds.addAll(generator.getWorlds()));

        return worlds;
    }

    public void addCompatibilityOptionsToCustomOreGen(CustomOreGen customOreGen) {
        List<String> disabledWorlds = customOreGen.getConfig().getStringList("disabled-worlds");
        List<String> reservedWorlds = this.getUsedWorlds();

        AtomicBoolean configurationChanged = new AtomicBoolean(false);
        reservedWorlds.forEach(world -> {
            if (disabledWorlds.contains(world)) {
                return;
            }

            disabledWorlds.add(world);

            if (!configurationChanged.get()) {
                configurationChanged.set(true);
            }
        });

        if (!configurationChanged.get()) {
            return;
        }

        try {
            customOreGen.getConfig().set("disabled-worlds", disabledWorlds);
            customOreGen.getConfig().save(new File(customOreGen.getDataFolder(), "config.yml"));
            this.plugin.dump("New CustomOreGen config:\n" + customOreGen.getConfig().saveToString());

            customOreGen.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
