package fr.bobinho.bcrate.api.entity;

import fr.bobinho.bcrate.api.location.BLocation;
import fr.bobinho.bcrate.api.metadata.BMetadata;
import fr.bobinho.bcrate.api.packet.BPacket;
import fr.bobinho.bcrate.api.renderer.BRenderer;
import fr.bobinho.bcrate.api.validate.BValidate;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * Bobinho entity library
 */
public class BEntity<T extends BEntity<T>> {

    /**
     * Fields
     */
    private final UUID uuid;
    private final Entity entity;
    private final BRenderer renderer;
    private final BMetadata metadata = new BMetadata();


    /**
     * Creates a new entity
     *
     * @param entity   the entity
     * @param location the location
     */
    public BEntity(@Nonnull Entity entity, @Nonnull Location location) {
        BValidate.notNull(entity);
        BValidate.notNull(location);

        this.uuid = UUID.randomUUID();
        this.entity = entity;
        this.entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.renderer = new BRenderer(location, this::show, this::hide);
    }

    /**
     * Creates a new entity
     *
     * @param entityType the entity type
     * @param location   the location
     */
    public BEntity(@Nonnull BEntityType entityType, @Nonnull Location location) {
        BValidate.notNull(entityType);
        BValidate.notNull(location);

        this.uuid = UUID.randomUUID();
        this.entity = entityType.create(location);
        this.entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.renderer = new BRenderer(location, this::show, this::hide);
    }

    /**
     * Gets the UUID
     *
     * @return the UUID
     */
    public final @Nonnull UUID getUID() {
        return uuid;
    }

    /**
     * Gets the entity
     *
     * @return the entity
     */
    protected @Nonnull Entity getEntity() {
        return entity;
    }

    /**
     * Gets the cast entity
     *
     * @param type the type
     * @return the cast entity
     */
    protected @Nonnull <E extends Entity> E getEntity(@Nonnull Class<E> type) {
        BValidate.notNull(type);

        return (E) entity;
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public final int getId() {
        return entity.getId();
    }

    /**
     * Gets the renderer
     *
     * @return the renderer
     */
    public final @Nonnull BRenderer getRenderer() {
        return renderer;
    }

    /**
     * Gets the metadata
     *
     * @return the metadata
     */
    public final @Nonnull BMetadata metadata() {
        return metadata;
    }

    /**
     * Updates the entity metadata
     */
    public final void updateMetadata() {
        BPacket.send(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true), renderer.getShownViewersAsPlayer());
    }

    /**
     * Sets the entity invisible status
     *
     * @param flag the invisible flag
     * @return the entity
     */
    public final @Nonnull T setInvisible(boolean flag) {
        entity.setInvisible(flag);

        return (T) this;
    }

    /**
     * Sets the entity invulnerable status
     *
     * @param flag the invulnerable flag
     * @return the entity
     */
    public final @Nonnull T setInvulnerable(boolean flag) {
        entity.setInvulnerable(flag);

        return (T) this;
    }

    /**
     * Sets the entity gravity status
     *
     * @param flag the gravity flag
     * @return the entity
     */
    public final @Nonnull T setNoGravity(boolean flag) {
        entity.setNoGravity(flag);

        return (T) this;
    }

    /**
     * Sets the entity silent status
     *
     * @param flag the silent flag
     * @return the entity
     */
    public final @Nonnull T setSilent(boolean flag) {
        entity.setSilent(flag);

        return (T) this;
    }

    /**
     * Shows the entity
     *
     * @param players the players
     */
    public final void show(@Nonnull List<Player> players) {
        BValidate.notNull(players);

        //Calls pre-show action, if it is false, no need to continue
        if (!onPreShow(players)) {
            return;
        }

        //Sends the show packet
        BPacket.send(new PacketPlayOutSpawnEntity(entity), players);
        BPacket.send(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true), players);

        //Triggers the on show
        onShow(players);
    }

    /**
     * Shows the entity
     */
    public final void show() {
        show((List<Player>) Bukkit.getOnlinePlayers().stream().filter(player -> BLocation.canSee(entity.getBukkitEntity().getLocation(), player.getLocation())).toList());
    }

    /**
     * Hides the entity
     *
     * @param players the players
     */
    public final void hide(@Nonnull List<Player> players) {
        BValidate.notNull(players);

        //Calls pre-hide action, if it is false, no need to continue
        if (!onPreHide(players)) {
            return;
        }

        //Sends the hide packet
        BPacket.send(new PacketPlayOutEntityDestroy(entity.getId()), players);

        //Triggers the on hide
        onHide(players);
    }

    /**
     * Hides the entity
     */
    public final void hide() {
        hide((List<Player>) Bukkit.getOnlinePlayers().stream().filter(player -> BLocation.canSee(entity.getBukkitEntity().getLocation(), player.getLocation())).toList());
    }

    /**
     * Triggers the render every tick
     */
    public final T render() {
        renderer.render();

        //Overridable method
        onTick();

        return (T) this;
    }

    /**
     * Removes the entity
     */
    public void remove() {
        hide(renderer.getShownViewersAsPlayer());
    }


    /*
    ACTIONS
     */

    /**
     * Triggers the update every tick
     */
    public void onTick() {
    }

    /**
     * Triggers when entity be shown
     *
     * @param players the players
     * @return true if we should continue, false otherwise
     */
    public boolean onPreShow(@Nonnull List<Player> players) {
        BValidate.notNull(players);

        return true;
    }

    /**
     * Triggers when entity be shown
     *
     * @param players the players
     */
    public void onShow(@Nonnull List<Player> players) {
        BValidate.notNull(players);
    }

    /**
     * Triggers when entity be hidden
     *
     * @param players the players
     * @return true if we should continue, false otherwise
     */
    public boolean onPreHide(@Nonnull List<Player> players) {
        BValidate.notNull(players);
        return true;
    }

    /**
     * Triggers when entity be hidden
     *
     * @param players the players
     */
    public void onHide(@Nonnull List<Player> players) {
        BValidate.notNull(players);
    }


    /*
    MISC
     */

    /**
     * Teleports the entity
     *
     * @param location the location
     * @return the entity
     */
    public @Nonnull T teleport(@Nonnull Location location) {
        BValidate.notNull(location);

        teleport(location, false);

        return (T) this;
    }

    /**
     * Teleports the entity
     *
     * @param location the location
     * @param rotation the special rotation packet
     */
    public void teleport(@Nonnull Location location, boolean rotation) {
        BValidate.notNull(location);

        //Sets base fields
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        renderer.setLocation(location);

        //If players are empty, no need to continue
        if (renderer.getShownViewersAsPlayer().isEmpty()) {
            return;
        }

        //Sends teleport packet
        BPacket.send(new PacketPlayOutEntityTeleport(entity), renderer.getShownViewersAsPlayer());

        //If special rotation needed
        if (rotation) {
            byte yaw = (byte) (location.getYaw() * 256.0F / 360.0F);

            //Sets head rotation.
            BPacket.send(new PacketPlayOutEntityHeadRotation(entity, yaw), renderer.getShownViewersAsPlayer());
        }
    }

}