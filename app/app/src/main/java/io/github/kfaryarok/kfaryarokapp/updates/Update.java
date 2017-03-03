package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * General interface for updates.
 *
 * Created by tbsc on 03/03/2017.
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

    /**
     * Used to know whether this update should be clickable, to view the whole update.
     * @return has long text
     */
    boolean hasLongText();

    /**
     * Make sure to check if there is a long text before doing this! {@link #hasLongText()}
     * @return The long text, that should be shown by clicking the card
     */
    String getLongText();

}
