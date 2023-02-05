package fr.bobinho.bcrate.api.notification;

import fr.bobinho.bcrate.api.color.BColor;
import fr.bobinho.bcrate.api.validate.BValidate;

import javax.annotation.Nonnull;

/**
 * Bobinho placeholder library
 */
public class BPlaceHolder {

    /**
     * Fields
     */
    private final String oldValue;
    private final String newValue;

    /**
     * Creates a new placeholder
     *
     * @param oldValue the old value
     * @param newValue the new value
     */
    public BPlaceHolder(@Nonnull String oldValue, @Nonnull String newValue) {
        BValidate.notNull(oldValue);
        BValidate.notNull(newValue);

        this.oldValue = oldValue;
        this.newValue = BColor.colour(newValue);
    }

    /**
     * Gets the value to be changed
     *
     * @return the value to be changed
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * Gets the value to be applied
     *
     * @return the value to be applied
     */
    public String getReplacement() {
        return newValue;
    }

}