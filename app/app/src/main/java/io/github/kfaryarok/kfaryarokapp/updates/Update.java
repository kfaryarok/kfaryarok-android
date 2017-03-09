package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * General interface for updates.
 *
 * @author tbsc on 03/03/2017
 */
public interface Update {

    /**
     * @return Who is affected by this update.
     */
    Affected getAffected();

    /**
     * @return The update text that should be displayed on the card itself.
     */
    String getText();

}
