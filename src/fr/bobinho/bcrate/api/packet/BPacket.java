package fr.bobinho.bcrate.api.packet;

import fr.bobinho.bcrate.BCrateCore;
import fr.bobinho.bcrate.api.validate.BValidate;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Bobinho packet library
 */
public final class BPacket {

    /**
     * Sends the packet to the players
     *
     * @param packet  the packet
     * @param players the players
     */
    public static void send(@Nonnull Packet<?> packet, @Nonnull Player... players) {
        BValidate.notNull(packet);
        BValidate.notNull(players);

        //Sends packet
        Arrays.stream(players)
                .filter(player -> player != null && player.isOnline())
                .forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
    }

    /**
     * Sends the packet to the player
     *
     * @param packet  the packet
     * @param players the uuids
     */
    public static void send(@Nonnull Packet<?> packet, @Nonnull UUID... players) {
        BValidate.notNull(packet);
        BValidate.notNull(players);

        //Sends packet
        Arrays.stream(players)
                .map(Bukkit::getPlayer)
                .filter(player -> player != null && player.isOnline())
                .forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
    }

    /**
     * Sends the packet to the player
     *
     * @param packet  the packet
     * @param players the players
     */
    public static void send(@Nonnull Packet<?> packet, @Nonnull List<Player> players) {
        BValidate.notNull(packet);
        BValidate.notNull(players);

        //Sends packet.
        players.stream()
                .filter(player -> player != null && player.isOnline())
                .forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
    }

    /**
     * Sends the packet to the location
     *
     * @param packet   the packet
     * @param location the location
     * @param radius   the radius
     */
    public static void send(@Nonnull Packet<?> packet, @Nonnull Location location, int radius) {
        BValidate.notNull(packet);
        BValidate.notNull(location);

        //Sends packet.
        Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, radius, radius, radius).stream()
                .filter(entity -> entity instanceof Player)
                .forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
    }

    /**
     * Gets the value from an object
     *
     * @param instance the instance
     * @param name     the name
     * @return Value.
     */
    public static @Nonnull Object getValue(@Nonnull Object instance, @Nonnull String name) {
        BValidate.notNull(instance);
        BValidate.notNull(name);

        try {
            //Gets field
            Field field = instance.getClass().getDeclaredField(name);
            boolean flag = field.canAccess(instance);

            //Allows accessible
            if (!flag) {
                field.setAccessible(true);
            }

            //Gets the value
            Object value = field.get(instance);

            //Disallows accessible
            if (!flag) {
                field.setAccessible(false);
            }

            return value;
        } catch (Exception exception) {
            BCrateCore.getBLogger().error("Couldn't get value from the object!", exception);
        }

        return instance;
    }

}
