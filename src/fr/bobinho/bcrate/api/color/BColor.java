package fr.bobinho.bcrate.api.color;

import fr.bobinho.bcrate.api.item.BItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Bobinho color library
 */
public final class BColor {

    /**
     * Unitilizable constructor (utility class)
     */
    private BColor() {
    }

    /**
     * Red colors
     */
    public static final String RED = ChatColor.of("#FF5555").toString();
    public static final String RED_BOLD = RED + "§l";

    public static final String DARK_RED = ChatColor.of("#AA0000").toString();
    public static final String DARK_RED_BOLD = DARK_RED + "§l";

    public static final String VERMILION = ChatColor.of("#E43D40").toString();
    public static final String VERMILION_BOLD = VERMILION + "§l";

    public static final String SCARLET = ChatColor.of("#FF0000").toString();
    public static final String SCARLET_BOLD = SCARLET + "§l";

    /**
     * Yellow colors
     */
    public static final String YELLOW = ChatColor.of("#FFFF55").toString();
    public static final String YELLOW_BOLD = YELLOW + "§l";

    public static final String GOLD = ChatColor.of("#FFAA00").toString();
    public static final String GOLD_BOLD = GOLD + "§l";

    public static final String AMBER = ChatColor.of("#FFD700").toString();
    public static final String AMBER_BOLD = AMBER + "§l";

    /**
     * Green colors
     */
    public static final String GREEN = ChatColor.of("#55FF55").toString();
    public static final String GREEN_BOLD = GREEN + "§l";

    public static final String DARK_GREEN = ChatColor.of("#00AA00").toString();
    public static final String DARK_GREEN_BOLD = DARK_GREEN + "§l";

    /**
     * Blue colors
     */
    public static final String BLUE = ChatColor.of("#5555FF").toString();
    public static final String BLUE_BOLD = BLUE + "§l";

    public static final String DARK_BLUE = ChatColor.of("#0000AA").toString();
    public static final String DARK_BLUE_BOLD = DARK_BLUE + "§l";

    public static final String BRIGHT_BLUE = ChatColor.of("#0E86D4").toString();
    public static final String BRIGHT_BLUE_BOLD = BRIGHT_BLUE + "§l";

    public static final String AQUA = ChatColor.of("#55FFFF").toString();
    public static final String AQUA_BOLD = AQUA + "§l";

    public static final String DARK_AQUA = ChatColor.of("#00AAAA").toString();
    public static final String DARK_AQUA_BOLD = DARK_AQUA + "§l";

    /**
     * Purple colors
     */
    public static final String LIGHT_PURPLE = ChatColor.of("#FF55FF").toString();
    public static final String LIGHT_PURPLE_BOLD = LIGHT_PURPLE + "§l";

    public static final String DARK_PURPLE = ChatColor.of("#AA00AA").toString();
    public static final String DARK_PURPLE_BOLD = DARK_PURPLE + "§l";

    /**
     * White colors
     */
    public static final String WHITE = ChatColor.of("#FFFFFF").toString();
    public static final String WHITE_BOLD = WHITE + "§l";

    /**
     * Gray colors
     */
    public static final String GRAY = ChatColor.of("#AAAAAA").toString();
    public static final String GRAY_BOLD = GRAY + "§l";

    public static final String DARK_GRAY = ChatColor.of("#555555").toString();
    public static final String DARK_GRAY_BOLD = DARK_GRAY + "§l";

    /**
     * BLACK colors
     */
    public static final String BLACK = ChatColor.of("#000000").toString();
    public static final String BLACK_BOLD = BLACK + "§l";

    /**
     * Gets the text without color
     *
     * @param text the text
     * @return the text without color
     */
    public static @Nonnull String discolour(@Nonnull String text) {
        return ChatColor.stripColor(text);
    }

    /**
     * Colours the text
     *
     * @param text the text
     * @return the colorized text
     */
    public static @Nonnull String colour(@Nonnull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Cleans the text color
     *
     * @param text the text
     * @return the cleaned text
     */
    public static @Nonnull String cleanColor(@Nonnull String text) {
        return new BItemBuilder(Material.STICK).name(colour(text)).build().getItemMeta().getDisplayName();
    }

}