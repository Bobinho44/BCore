package fr.bobinho.bcrate.util.crate;

import fr.bobinho.bcrate.api.entity.base.BArmorStand;
import fr.bobinho.bcrate.api.item.BItemBuilder;
import fr.bobinho.bcrate.api.location.BLocation;
import fr.bobinho.bcrate.api.metadata.BMetadata;
import fr.bobinho.bcrate.api.notification.BPlaceHolder;
import fr.bobinho.bcrate.api.scheduler.BScheduler;
import fr.bobinho.bcrate.api.validate.BValidate;
import fr.bobinho.bcrate.util.crate.edit.color.Color;
import fr.bobinho.bcrate.util.crate.edit.size.Size;
import fr.bobinho.bcrate.util.crate.notification.CrateNotification;
import fr.bobinho.bcrate.util.crate.ux.CrateEditMenu;
import fr.bobinho.bcrate.util.crate.ux.CratePrizeMenu;
import fr.bobinho.bcrate.util.crate.ux.CrateShowMenu;
import fr.bobinho.bcrate.util.key.Key;
import fr.bobinho.bcrate.util.prize.Prize;
import fr.bobinho.bcrate.wrapper.MonoValuedAttribute;
import fr.bobinho.bcrate.wrapper.MultiValuedAttribute;
import fr.bobinho.bcrate.wrapper.ReadOnlyMonoValuedAttribute;
import fr.bobinho.bcrate.wrapper.UpperBoundedMultiValuedAttribute;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the crate
 */
public class Crate {

    /**
     * Fields
     */
    private final MonoValuedAttribute<String> name;
    private final MonoValuedAttribute<Size> size;
    private final UpperBoundedMultiValuedAttribute<Prize> prizes;
    private final ReadOnlyMonoValuedAttribute<Location> location;
    private final MonoValuedAttribute<Color> color;
    private final MonoValuedAttribute<Key> key;
    private final ReadOnlyMonoValuedAttribute<CrateEditMenu> editMenu;
    private final ReadOnlyMonoValuedAttribute<CratePrizeMenu> prizeMenu;
    private final ReadOnlyMonoValuedAttribute<CrateShowMenu> showMenu;
    private final MultiValuedAttribute<BArmorStand> structure;
    private final BScheduler animation;
    private final BMetadata metadata;

    /**
     * Creates a new crate
     *
     * @param name     the name
     * @param size     the size
     * @param prizes   the prizes
     * @param location the location
     * @param color    the color
     */
    public Crate(@Nonnull String name, @Nonnull Size size, @Nonnull List<Prize> prizes, @Nonnull Location location, @Nonnull Color color, @Nonnull Key key, @Nonnull List<BArmorStand> structure) {
        BValidate.notNull(name);
        BValidate.notNull(size);
        BValidate.notNull(prizes);
        BValidate.notNull(color);
        BValidate.notNull(key);
        BValidate.notNull(structure);

        this.name = new MonoValuedAttribute<>(name);
        this.size = new MonoValuedAttribute<>(size);
        this.prizes = new UpperBoundedMultiValuedAttribute<>(size.getDimension(), prizes);
        this.location = new ReadOnlyMonoValuedAttribute<>(location);
        this.color = new MonoValuedAttribute<>(color);
        this.key = new MonoValuedAttribute<>(key);
        this.structure = new MultiValuedAttribute<>(structure);
        this.editMenu = new ReadOnlyMonoValuedAttribute<>(new CrateEditMenu(this));
        this.prizeMenu = new ReadOnlyMonoValuedAttribute<>(new CratePrizeMenu(this));
        this.showMenu = new ReadOnlyMonoValuedAttribute<>(new CrateShowMenu(this));
        this.metadata = new BMetadata().add("spine").set("spine:degree", 0.0F);

        this.animation = BScheduler.syncScheduler().every(2);
        this.animation.run(() -> {

            //Opens animation
            if (metadata.has("open") && !metadata.has("restart")) {
                double degree = metadata.getNonNull("open:degree", double.class);
                int i = (int) (degree / 10);

                //Restarts and give prizes
                if (degree > 130) {
                    restart();
                }

                else {
                    Random r = new Random();
                    for (int j = 0; j < 8; j++) {
                        location.getWorld().spawnParticle(
                                Particle.REDSTONE,
                                location.clone().add(0.0D, 2.0D, 0.0D),
                                1,
                                0.1,
                                0.1,
                                0.1,
                                new Particle.DustOptions(org.bukkit.Color.fromBGR(r.nextInt(256), r.nextInt(256), r.nextInt(256)), 2));
                    }

                    structure.get(Math.max(2, (int) (degree / 10 + 2) - 1)).clearEquipment();
                    structure.get(Math.max(16, 13 + (int) (degree / 10 + 2))).clearEquipment();

                    structure.get((int) (degree / 10 + 2)).setEquipment(new BItemBuilder(Material.DIAMOND_SHOVEL).durability(27).build());
                    structure.get(14 + (int) (degree / 10 + 2)).setEquipment(new BItemBuilder(Material.DIAMOND_SHOVEL).durability(28).build());
                }

                metadata.set("open:degree", degree + 10.0D);
            }

            //Spines animation
            else if (metadata.has("spine")) {
                float degree = metadata.getNonNull("spine:degree", float.class);

                if (metadata.has("waitOpen")) {

                    location.getWorld().spawnParticle(Particle.CLOUD, location, 10, 0.3, 0.3, 0.3);

                    //Opens the crate
                    if (degree == 0) {
                        open();
                    }
                }

                if (!metadata.has("open")) {
                    //Gets target location
                    Location newLocation = this.location.get().add(0, -Math.abs((degree - 180) / 360) + 0.5, 0);
                    newLocation.setPitch(0.0F);
                    newLocation.setYaw(BLocation.degreeToYaw(degree));

                    structure.get(0).teleport(newLocation);

                    metadata.set("spine:degree", (degree + 10.0F) % 360);
                }
            }
        });
    }

    /**
     * Creates a new crate
     *
     * @param name the name
     * @param size the size
     */
    public Crate(@Nonnull String name, @Nonnull Size size, @Nonnull Location location, @Nonnull Color color, @Nonnull Key key, @Nonnull List<BArmorStand> structure) {
        this(name, size, new ArrayList<>(), location, color, key, structure);
    }

    /**
     * Gets the name wrapper
     *
     * @return the name wrapper
     */
    public @Nonnull MonoValuedAttribute<String> name() {
        return name;
    }

    /**
     * Gets the size wrapper
     *
     * @return the size wrapper
     */
    public @Nonnull MonoValuedAttribute<Size> size() {
        return size;
    }

    /**
     * Gets the prizes wrapper
     *
     * @return the prizes wrapper
     */
    public @Nonnull UpperBoundedMultiValuedAttribute<Prize> prizes() {
        return prizes;
    }

    /**
     * Gets the linked block wrapper
     *
     * @return the linked block wrapper
     */
    public @Nonnull ReadOnlyMonoValuedAttribute<Location> location() {
        return location;
    }

    /**
     * Gets the color wrapper
     *
     * @return the color wrapper
     */
    public @Nonnull MonoValuedAttribute<Color> color() {
        return color;
    }

    /**
     * Gets the key wrapper
     *
     * @return the key wrapper
     */
    public @Nonnull MonoValuedAttribute<Key> key() {
        return key;
    }

    /**
     * Gets the edit menu wrapper
     *
     * @return the edit menu wrapper
     */
    public @Nonnull ReadOnlyMonoValuedAttribute<CrateEditMenu> editMenu() {
        return editMenu;
    }

    /**
     * Gets the prize menu wrapper
     *
     * @return the prize menu wrapper
     */
    public @Nonnull ReadOnlyMonoValuedAttribute<CratePrizeMenu> prizeMenu() {
        return prizeMenu;
    }

    /**
     * Gets the play menu wrapper
     *
     * @return the play menu wrapper
     */
    public @Nonnull ReadOnlyMonoValuedAttribute<CrateShowMenu> showMenu() {
        return showMenu;
    }

    /**
     * Gets the structure wrapper
     *
     * @return the structure wrapper
     */
    public @Nonnull MultiValuedAttribute<BArmorStand> structure() {
        return structure;
    }

    /**
     * Gets the metadata wrapper
     *
     * @return the metadata wrapper
     */
    public BMetadata metadata() {
        return metadata;
    }

    /**
     * Gets the animation wrapper
     *
     * @return the animation wrapper
     */
    public BScheduler animation() {
        return animation;
    }

    /**
     * Launchs the animation to open the crate
     */
    private void open() {
        structure.get(0).clearEquipment();
        structure.get(1).setEquipment(new BItemBuilder(Material.DIAMOND_SHOVEL).durability(29).build());

        metadata.remove("spine").remove("waitOpen").add("open").set("open:degree", 0.0D);
    }

    /**
     * Restarts the crate and give prizes to the last player
     */
    private void restart() {
        metadata.add("restart");
        Player player = metadata.getNonNull("player", Player.class);
        ItemStack[] items = metadata.getNonNull("prizes", ItemStack[].class);

        Vector v = player.getLocation().toVector().subtract(location.get().add(0, 1, 0).toVector()).normalize().divide(new Vector(3, 3, 3));

        for (ItemStack item : items) {
            location.get().getWorld().dropItem(location.get().add(0, 2, 0), item, drop -> {
                drop.setPickupDelay(99999);
                BScheduler.syncScheduler().after(2, TimeUnit.SECONDS).run(drop::remove);
                BScheduler.syncScheduler().after(1, TimeUnit.SECONDS).run(() -> drop.setGravity(true));
            }).setVelocity(v);
        }

        BScheduler.asyncScheduler().after(2, TimeUnit.SECONDS).run(() -> {

            //Messages
            player.sendMessage(CrateNotification.CRATE_WON.getNotification());
            for (ItemStack item : items) {
                player.sendMessage(CrateNotification.CRATE_PRIZE_INFO.getNotification(
                        new BPlaceHolder("%amount%", String.valueOf(item.getAmount())),
                        new BPlaceHolder("%name%", (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getType().name().replace("_", " "))));
            }

            //Gives prizes
            player.getInventory().addItem(items);

            metadata.remove("player").remove("prizes").remove("open").remove("open:degree").remove("restart").add("spine");

            structure.get(0).setEquipment(new BItemBuilder(Material.DIAMOND_SHOVEL).durability(26).build());
            List.of(1, 15, 29).forEach(i -> structure.get(i).clearEquipment());
        });
    }

    /**
     * Launchs the crate's animation and wait for the crate to return to its original position
     *
     * @param player the player
     * @param prizes the prizes
     */
    public void wait(@Nonnull Player player, @Nonnull ItemStack[] prizes) {
        BValidate.notNull(player);
        BValidate.notNull(prizes);

        player.getInventory().removeItem(key.get().item().get());
        metadata.add("waitOpen").set("prizes", prizes).set("player", player);
    }

}