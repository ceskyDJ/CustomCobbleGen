# Custom Cobble Gen

Custom Cobble Gen is Spigot plugin that provides option to change cobble generator to some other block one. Primary use of this plugin is in specific types of worlds like nether and end, where isn't standard skyblock islands.

This is a fork of [pkt77/OreGenerator](https://github.com/pkt77/OreGenerator) repository. Thanks to pkt77 for basis of this project.

## Commands
- **/ccgreload** or **/ccgr** - reload plugin configuration

## Permissions
- **customcobblegen.use.WORLD** - use configured generators in some world  
- **customcobblegen.use.*** - use generators in all worlds (allowed in generators's config)
- **customcobblegen.reload** - use /ccgreload (or /ccgr) command
- **customcobblegen.*** - all permissions of this plugin

## Compatibility
Custom Cobble Gen is compatible with these plugins:
- **Multiverse-Core** - prefered multi world plugin
- **CustomOreGen** - another cobble stone generator modifier. This plugin automatically disable it in world where is used Custom Cobble Gen. Custom Cobble Gen is primary for specific worlds like nether and end. In this type of worlds CustomOreGen doesn't work at all. Its disabling is just in case.

## Download and installation
1. Go to the latest release page and download the JAR file https://github.com/ceskyDJ/CustomCobbleGen/releases/latest
2. Put the file CustomCobbleGen-1.X.X to your server's plugins/ directory.
3. Restart your server
4. You are done! Now you can change configuration of the plugin in plugins/CustomCobbleGen directory. If there is some problem, you can insert there default configuration from this page or create an issue.

If you don't want to find the JAR, here's the direct link: [CustomCobbleGen-1.0.1.jar](https://github.com/ceskyDJ/CustomCobbleGen/releases/download/v1.0.1/CustomCobbleGen-1.0.1.jar)

## Default configuration
```YAML
# Allow using debug dumps. It's not recommended on production servers.
debug: false
# Generators
# You can duplicate default 'nether' generator and create as many generators as you want
generators:
  # Generator name
  nether:
    # Allowed worlds - supports Multiverse-Core (primarily) and other multi world plugins
    worlds:
      - skyworld_nether
    # Generated blocks
    blocks:
      # Use: BLOCK_NAME:CHANCE
      #   For BLOCK_NAME use bukkit names. See https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html.
      #   CHANCE is decimal number from 0-100
      #   Sum of the chances mustn't be more than 100
      - NETHERRACK:90.0
      - SOUL_SAND:10.0
# Messages for translation
# Change only text in quotation marks ("")
# If you need to use quotation mark in your text, use \" instead of "
messages:
  permissions: "You do not have permission to do that!"
  config-reload: "Custom Cobble Gen configuration reloaded!"
```
