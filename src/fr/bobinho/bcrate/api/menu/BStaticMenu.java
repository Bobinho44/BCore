package fr.bobinho.bcrate.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class BStaticMenu extends BMenu {


    /**
     * Creates a new static menu
     *
     * @param size the menu size
     */
    public BStaticMenu(int size) {
        super(size);

        fillInventory().run();
    }

    /**
     * Creates a new static menu
     *
     * @param type the menu type
     */
    public BStaticMenu(@Nonnull InventoryType type) {
        super(type);

        fillInventory().run();
    }

    /**
     * Creates a new static menu
     *
     * @param type  the menu type
     * @param title the menu title
     */
    public BStaticMenu(@Nonnull InventoryType type, @Nonnull String title) {
        super(type, title);

        fillInventory().run();
    }

    /**
     * Creates a new static menu
     *
     * @param size  the menu size
     * @param title the menu title
     */
    public BStaticMenu(int size, @Nonnull String title) {
        super(size, title);

        fillInventory().run();
    }

    @Override
    public void openInventory(@NotNull Player player) {
        player.openInventory(getInventory());
    }

    public abstract Runnable fillInventory();

}
