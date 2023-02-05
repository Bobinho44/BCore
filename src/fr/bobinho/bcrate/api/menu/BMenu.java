package fr.bobinho.bcrate.api.menu;

import fr.bobinho.bcrate.api.event.BEvent;
import fr.bobinho.bcrate.api.validate.BValidate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Bobinho menu library
 */
public abstract class BMenu implements InventoryHolder {

    /**
     * Fields
     */
    private Inventory inventory;
    private String title;

    /**
     * Creates a new menu
     *
     * @param size the menu size
     */
    public BMenu(int size) {
        this.inventory = Bukkit.createInventory(this, size);
    }

    /**
     * Creates a new menu
     *
     * @param type the menu type
     */
    public BMenu(@Nonnull InventoryType type) {
        BValidate.notNull(type);

        this.inventory = Bukkit.createInventory(this, type);
    }

    /**
     * Creates a new menu
     *
     * @param type  the menu type
     * @param title the menu title
     */
    public BMenu(@Nonnull InventoryType type, @Nonnull String title) {
        BValidate.notNull(type);
        BValidate.notNull(title);

        this.inventory = Bukkit.createInventory(this, type, title);
        this.title = title;
    }

    /**
     * Creates a new menu
     *
     * @param size  the menu size
     * @param title the menu title
     */
    public BMenu(int size, @Nonnull String title) {
        BValidate.notNull(title);

        this.inventory = Bukkit.createInventory(this, size, title);
        this.title = title;
    }

    /**
     * Resizes the menu
     *
     * @param size the size
     */
    public void resize(int size) {
        inventory = Bukkit.createInventory(this, size, title != null ? title : "");
    }

    /**
     * Adds an item to the menu
     *
     * @param slot the slot
     * @param item the item
     */
    public void setItem(int slot, @Nonnull ItemStack item) {
        BValidate.notNull(item);

        if (slot >= getInventory().getSize()) {
            ItemStack[] copy = inventory.getContents();

            resize(((int) (slot / 9.0D) + 1) * 9);

            for (int i = 0; i < copy.length; i++) {
                inventory.setItem(i, copy[i]);
            }
        }

        getInventory().setItem(slot, item);
    }

    public void addItemEvent(int slot, ClickType clickType, Runnable runnable) {
        BEvent.registerEvent(InventoryClickEvent.class)
                .filter(event -> Objects.equals(event.getInventory().getHolder(), this))
                .filter(event -> event.getSlot() == slot)
                .filter(event -> event.getClick() == clickType)
                .consume(event -> {
                    event.setCancelled(true);

                    runnable.run();
                });
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull Inventory getInventory() {
        return inventory;
    }

    /**
     * Opens the menu
     *
     * @param player the player
     */
    public abstract void openInventory(@Nonnull Player player);

}
