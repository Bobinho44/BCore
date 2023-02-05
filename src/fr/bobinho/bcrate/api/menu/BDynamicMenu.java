package fr.bobinho.bcrate.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class BDynamicMenu extends BMenu {


    /**
     * Creates a new dynamic menu
     *
     * @param size the menu size
     */
    public BDynamicMenu(int size) {
        super(size);
    }

    /**
     * Creates a new dynamic menu
     *
     * @param type the menu type
     */
    public BDynamicMenu(@Nonnull InventoryType type) {
        super(type);
    }

    /**
     * Creates a new dynamic menu
     *
     * @param type  the menu type
     * @param title the menu title
     */
    public BDynamicMenu(@Nonnull InventoryType type, @Nonnull String title) {
        super(type, title);
    }

    /**
     * Creates a new dynamic menu
     *
     * @param size  the menu size
     * @param title the menu title
     */
    public BDynamicMenu(int size, @Nonnull String title) {
        super(size, title);
    }

    @Override
    public void openInventory(@NotNull Player player) {
        fillInventory().accept(player);
        player.openInventory(getInventory());
    }

    public abstract Consumer<Player> fillInventory();

}
