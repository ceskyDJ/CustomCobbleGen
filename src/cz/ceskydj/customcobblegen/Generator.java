package cz.ceskydj.customcobblegen;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Generator {
    private String name;
    private List<String> worlds;
    private Map<Material, Double> blocks;

    public Generator(String name, List<String> worlds, Map<Material, Double> blocks) {
        this.name = name;
        this.worlds = worlds;
        this.blocks = blocks;
    }

    public String getName() {
        return name;
    }

    public List<String> getWorlds() {
        return this.worlds;
    }

    public Map<Material, Double> getBlocks() {
        return this.blocks;
    }
}
