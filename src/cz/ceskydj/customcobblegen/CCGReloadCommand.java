package cz.ceskydj.customcobblegen;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCGReloadCommand implements CommandExecutor {
	private final CustomCobbleGen plugin;
	private final ConfigManager configManager;

	public CCGReloadCommand(CustomCobbleGen customCobbleGen) {
		this.plugin = customCobbleGen;

		this.configManager = this.plugin.getConfigManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!sender.hasPermission("customcobblegen.reload")) {
			sender.sendMessage(ChatColor.RED + this.configManager.getMessage("permissions"));
		} else {
			this.configManager.reloadConfig();
			sender.sendMessage(ChatColor.GREEN + this.configManager.getMessage("config-reload"));
		}

		return true;
	}
}