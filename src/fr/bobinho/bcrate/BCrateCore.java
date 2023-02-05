package fr.bobinho.bcrate;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.bcrate.api.command.BCommand;
import fr.bobinho.bcrate.api.logger.BLogger;
import fr.bobinho.bcrate.api.setting.BSetting;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Core of the plugin
 */
public final class BCrateCore extends JavaPlugin {

    /**
     * Fields
     */
    private static final BLogger bLogger = new BLogger(BCrateCore.class.getSimpleName());
    private static BSetting keySetting;
    private static BSetting tagSetting;
    private static BSetting playerSetting;
    private static BSetting crateSetting;
    private static BSetting langSetting;

    /**
     * Gets the plugin
     *
     * @return the plugin
     */
    public static BCrateCore getInstance() {
        return JavaPlugin.getPlugin(BCrateCore.class);
    }

    /**
     * Gets the logger
     *
     * @return the logger
     */
    public static BLogger getBLogger() {
        return bLogger;
    }

    /**
     * Gets the key setting
     *
     * @return the key setting
     */
    public static BSetting getKeySetting() {
        return keySetting;
    }

    /**
     * Gets the tag setting
     *
     * @return the tag setting
     */
    public static BSetting getTagSetting() {
        return tagSetting;
    }

    /**
     * Gets the player setting
     *
     * @return the player setting
     */
    public static BSetting getPlayerSetting() {
        return playerSetting;
    }

    /**
     * Gets the crate setting
     *
     * @return the crate setting
     */
    public static BSetting getCrateSetting() {
        return crateSetting;
    }

    /**
     * Gets the lang setting
     *
     * @return the lang setting
     */
    public static BSetting getLangSetting() {
        return langSetting;
    }

    /**
     * Enables and initializes the plugin
     */
    @Override
    public void onEnable() {
        bLogger.info("Loading the plugin...");

        keySetting = new BSetting("key");
        tagSetting = new BSetting("tag");
        playerSetting = new BSetting("player");
        crateSetting = new BSetting("crate");
        langSetting = new BSetting("lang");

        //Registers commands
        registerCommands();
    }

    /**
     * Disables the plugin and save data
     */
    @Override
    public void onDisable() {
        bLogger.info("Unloading the plugin...");
    }

    /**
     * Registers commands
     */
    private void registerCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        Reflections reflections = new Reflections("fr.bobinho.bcrate.commands");
        Set<Class<? extends BCommand>> classes = reflections.getSubTypesOf(BCommand.class);
        for (@Nonnull Class<? extends BCommand> command : classes) {
            try {
                commandManager.registerCommand(command.getDeclaredConstructor().newInstance());
            } catch (Exception exception) {
                getBLogger().error("Couldn't register command(" + command.getName() + ")!", exception);
            }
        }
        bLogger.info("Successfully loaded commands.");
    }

}
