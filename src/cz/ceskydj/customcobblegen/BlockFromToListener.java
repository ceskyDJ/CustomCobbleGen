package cz.ceskydj.customcobblegen;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class BlockFromToListener implements Listener {
    private final CustomCobbleGen plugin;
    private final ConfigManager configManager;

    public BlockFromToListener(CustomCobbleGen customCobbleGen) {
        this.plugin = customCobbleGen;

        this.configManager = this.plugin.getConfigManager();
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        Material blockMaterial = event.getBlock().getType();
        if (!(blockMaterial == Material.WATER || blockMaterial == Material.LAVA)) {
            return;
        }

        Block generatedBlock = event.getToBlock();
        if (generatedBlock.getType() != Material.AIR) {
            return;
        }

        if (!this.isCobbleGenerator(blockMaterial, generatedBlock)) {
            return;
        }

        Player player = this.plugin.getClosestPlayer(event.getToBlock().getLocation());
        if (!(player.hasPermission("customcobblegen.use." + event.getToBlock().getWorld().getName()) || player.hasPermission("customcobblegen.use.*"))) {
            return;
        }

        Generator generator = this.configManager.getGenerator(event.getToBlock().getWorld().getName());
        if (generator == null) {
            return;
        }
        this.plugin.dump("Using generator '" + generator.getName() + "'");

        event.setCancelled(true);

        generatedBlock.setType(this.selectMaterialFromGenerator(generator));
    }

    private boolean isCobbleGenerator(Material firstLiquid, Block generatedBlock) {
        Material secondLiquid = (firstLiquid == Material.WATER ? Material.LAVA : Material.WATER);

        BlockFace[] faces = {BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        // At some face of the generated block has to be water or lava (by secondLiquid)
        for (BlockFace face : faces) {
            Block blockNextToGenerated = generatedBlock.getRelative(face, 1);
            if (blockNextToGenerated.getType() == secondLiquid) {
                return true;
            }
        }

        return false;
    }

    private Material selectMaterialFromGenerator(Generator generator) {
        Random random = new Random();

        List<Material> materials = new ArrayList<>();
        generator.getBlocks().forEach(((material, chance) -> {
            for (int i = 0; i < chance; i++) {
                materials.add(material);
            }
        }));

        int selectedIndex = random.nextInt(materials.size());
        this.plugin.dump("Materials list: " + materials.toString());
        this.plugin.dump("Selected index of materials list: " + selectedIndex);

        return materials.get(selectedIndex);
    }
}