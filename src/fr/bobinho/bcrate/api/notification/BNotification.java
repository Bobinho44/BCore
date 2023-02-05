package fr.bobinho.bcrate.api.notification;

import fr.bobinho.bcrate.api.color.BColor;
import fr.bobinho.bcrate.api.setting.BSetting;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Bobinho notification library
 */
public interface BNotification {

    BSetting getSettingFile();

    String getNotificationName();

    /**
     * Gets the text of the notification
     *
     * @return the text of the notification
     */
    default String getNotification() {
        return BColor.colour(getSettingFile().getString(getNotificationName()));
    }

    /**
     * Gets the text of the notification after modification via placeholders
     *
     * @param placeholders all placeholders
     * @return the text of the notification after modification via placeholders
     */
    default String getNotification(@Nonnull BPlaceHolder... placeholders) {
        String notification = getSettingFile().getString(getNotificationName());

        for (BPlaceHolder placeHolder : placeholders) {
            notification = notification.replaceAll(placeHolder.getOldValue(), placeHolder.getReplacement());
        }

        return BColor.colour(notification);
    }

    /**
     * Gets the text list of the notification after modification via placeholders
     *
     * @param placeholders all placeholders
     * @return the text of the notification after modification via placeholders
     */
    default List<String> getNotifications(@Nonnull BPlaceHolder... placeholders) {
        List<String> notifications = getSettingFile().getStringList(getNotificationName()).stream().toList();

        return notifications.stream()
                .map(notification -> {
                    for (BPlaceHolder placeHolder : placeholders) {
                        notification = notification.replaceAll(placeHolder.getOldValue(), placeHolder.getReplacement());
                    }

                    return BColor.colour(notification);
                })
                .toList();
    }

}
