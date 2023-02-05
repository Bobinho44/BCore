package fr.bobinho.bcrate.api.entity.base;

import com.mojang.datafixers.util.Pair;
import fr.bobinho.bcrate.api.entity.BEntity;
import fr.bobinho.bcrate.api.entity.BEntityType;
import fr.bobinho.bcrate.api.item.BItemBuilder;
import fr.bobinho.bcrate.api.packet.BPacket;
import fr.bobinho.bcrate.api.validate.BValidate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Bobinho armored entity library
 */
public class BArmoredEntity<T extends BArmoredEntity<T>> extends BEntity<BArmoredEntity<T>> {

    /**
     * Equipments
     */
    public enum Equipment {
        HELMET(EnumItemSlot.f),
        CHESTPLATE(EnumItemSlot.e),
        LEGGINGS(EnumItemSlot.d),
        BOOTS(EnumItemSlot.c),
        MAIN_HAND(EnumItemSlot.a),
        OFF_HAND(EnumItemSlot.b);

        /**
         * Fields
         */
        private final EnumItemSlot slot;

        /**
         * Creates a new equipment
         *
         * @param slot the equipment slot
         */
        Equipment(@Nonnull EnumItemSlot slot) {
            BValidate.notNull(slot);

            this.slot = slot;
        }

        /**
         * Gets the equipment slot
         *
         * @return the equipment slot
         */
        public @Nonnull EnumItemSlot getSlot() {
            return this.slot;
        }

        /**
         * Gets the slot for the target item packet
         *
         * @param targetItem the target Item
         * @return the slot for the target item packet
         */
        public @Nonnull List<Pair<EnumItemSlot, ItemStack>> toPacketObject(@Nonnull org.bukkit.inventory.ItemStack targetItem) {
            BValidate.notNull(targetItem);

            return new ArrayList<>(List.of(new Pair<>(this.slot, CraftItemStack.asNMSCopy(targetItem))));
        }
    }

    /**
     * Fields
     */
    private final HashMap<Equipment, org.bukkit.inventory.ItemStack> equipments = new HashMap<>();

    /**
     * Creates a new armored entity
     *
     * @param entityType the entity type
     * @param location   the location
     */
    public BArmoredEntity(@Nonnull BEntityType entityType, @Nonnull Location location) {
        super(entityType, location);
    }

    /**
     * Gets the equipment
     *
     * @param equipment the equipment
     * @return the equipment
     */
    public final @Nonnull Optional<org.bukkit.inventory.ItemStack> getEquipment(@Nonnull Equipment equipment) {
        BValidate.notNull(equipment);
        return Optional.ofNullable(this.equipments.get(equipment));
    }

    /**
     * Sets the equipment
     *
     * @param equipment the equipment
     * @param item      the item
     * @return the armored entity
     */
    public final @Nonnull T setEquipment(@Nonnull Equipment equipment, @Nonnull org.bukkit.inventory.ItemStack item) {
        BValidate.notNull(equipment);
        BValidate.notNull(item);

        //Sets equipment
        equipments.put(equipment, item);

        //If players are empty, no need to continue
        if (!getRenderer().getShownViewersAsPlayer().isEmpty()) {

            //Sends equipment packet
            BPacket.send(new PacketPlayOutEntityEquipment(getId(), equipment.toPacketObject(item)), getRenderer().getShownViewersAsPlayer());
        }

        return (T) this;
    }

    /**
     * Clears the equipment item
     *
     * @param equipment the equipment
     */
    public final T clearEquipment(@Nonnull Equipment equipment) {
        BValidate.notNull(equipment);

        Optional.ofNullable(equipments.remove(equipment)).ifPresent(previousItem -> {

            //If players are empty, no need to continue
            if (getRenderer().getShownViewersAsPlayer().isEmpty())
                return;

            //Sends the equipment clear packet
            BPacket.send(new PacketPlayOutEntityEquipment(getId(), equipment.toPacketObject(new BItemBuilder(Material.AIR).build())), getRenderer().getShownViewersAsPlayer());
        });

        return (T) this;
    }

    /**
     * Clears equipments
     */
    public final T clearEquipments() {
        Arrays.stream(Equipment.values()).forEach(this::clearEquipment);

        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onShow(@Nonnull List<Player> players) {
        BValidate.notNull(players);

        //Equipment packets
        Arrays.stream(Equipment.values()).forEach(equipment ->
                this.getEquipment(equipment).ifPresent(equipmentItem -> {

                    //Sends equipment packet
                    BPacket.send(new PacketPlayOutEntityEquipment(getId(), equipment.toPacketObject(equipmentItem)), players);
                })
        );
    }

}