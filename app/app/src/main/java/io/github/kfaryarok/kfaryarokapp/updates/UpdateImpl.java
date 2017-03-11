package io.github.kfaryarok.kfaryarokapp.updates;

import java.util.List;

/**
 * In true OOP-fashion, new class for containing new updates, that will be given to the adapter
 * for creating new cards.
 *
 * Most of the class works by using different constructor for different updates.
 * You have {@link #UpdateImpl(String[], String)} for when you want to create
 * an update for only some classes, and {@link #UpdateImpl(String)} for when you want
 * to create a global update.
 *
 * @author tbsc on 03/03/2017
 */
public class UpdateImpl implements Update {

    private Affected mAffected;
    private String mSummary;

    /**
     * Constructor for creating an update, that affects only certain classes.
     *
     * @param affectedClasses Array of classes that this update affects.
     *                        If, for example, I'm in E7, and this update affects
     *                        E6, I'm not one of the affected classes. But if it
     *                        affects E7, then I should see this update.
     * @param text A short text explaining the update
     */
    public UpdateImpl(String[] affectedClasses, String text) {
        this.mAffected = new ClassesAffected(affectedClasses);
        this.mSummary = text;
    }

    /**
     * Constructor for creating an update, that affects only certain classes.
     *
     * @param affectedClasses List of classes that this update affects.
     *                        If, for example, I'm in E7, and this update affects
     *                        E6, I'm not one of the affected classes. But if it
     *                        affects E7, then I should see this update.
     * @param text A short text explaining the update
     */
    public UpdateImpl(List<String> affectedClasses, String text) {
        this.mAffected = new ClassesAffected(affectedClasses.toArray(new String[0]));
        this.mSummary = text;
    }

    /**
     * Constructor for creating a global update.
     *
     * @param text what to display
     */
    public UpdateImpl(String text) {
        this.mAffected = new GlobalAffected();
        this.mSummary = text;
    }

    /**
     * Creates an update with no specific affection.
     *
     * @param affects who this affects
     * @param text short text to display on card
     */
    public UpdateImpl(Affected affects, String text) {
        this.mAffected = affects;
        this.mSummary = text;
    }

    @Override
    public Affected getAffected() {
        return mAffected;
    }

    @Override
    public String getText() {
        return mSummary;
    }

}
