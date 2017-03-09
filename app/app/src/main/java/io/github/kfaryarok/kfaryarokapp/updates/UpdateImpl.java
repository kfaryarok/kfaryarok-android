package io.github.kfaryarok.kfaryarokapp.updates;

import java.util.List;

/**
 * In true OOP-fashion, new class for containing new updates, that will be given to the adapter
 * for creating new cards.
 *
 * Most of the class works by using different constructor for different updates.
 * You have {@link #UpdateImpl(String[], String, String)} and {@link #UpdateImpl(String[], String)} for
 * when you want to create an update for only some classes, and {@link #UpdateImpl(String)} and
 * {@link #UpdateImpl(String, String)} for when you want to create a global update.
 *
 * @author tbsc on 03/03/2017
 */
public class UpdateImpl implements Update {

    private Affected mAffected;
    private String mSummary;
    private boolean mHasLongText;
    private String mLongText;

    /**
     * Empty constructor
     */
    public UpdateImpl() {

    }

    /**
     * Constructor for creating an update, that affects only certain classes,
     * with only a short text as the update, with no extra text to show.
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
        this.mHasLongText = false;
    }

    /**
     * Constructor for creating an update, that affects only certain classes,
     * with a summary of the text shown, and an option to view a long version
     * of the update, by clicking on the card.
     *
     * @param affectedClasses read {@link #UpdateImpl(String[], String)}
     * @param summary short summary of the long text
     * @param longText the entire update
     */
    public UpdateImpl(String[] affectedClasses, String summary, String longText) {
        this.mAffected = new ClassesAffected(affectedClasses);
        this.mSummary = summary;
        this.mHasLongText = true;
        this.mLongText = longText;
    }

    /**
     * Constructor for creating an update, that affects only certain classes,
     * with only a short text as the update, with no extra text to show.
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
        this.mHasLongText = false;
    }

    /**
     * Constructor for creating an update, that affects only certain classes,
     * with a summary of the text shown, and an option to view a long version
     * of the update, by clicking on the card.
     *
     * @param affectedClasses read {@link #UpdateImpl(List, String)}
     * @param summary short summary of the long text
     * @param longText the entire update
     */
    public UpdateImpl(List<String> affectedClasses, String summary, String longText) {
        this.mAffected = new ClassesAffected(affectedClasses.toArray(new String[0]));
        this.mSummary = summary;
        this.mHasLongText = true;
        this.mLongText = longText;
    }

    /**
     * Constructor for creating a global update, with only a short text
     * displayed on the card, with no "view long version".
     *
     * @param text what to display
     */
    public UpdateImpl(String text) {
        this.mAffected = new GlobalAffected();
        this.mSummary = text;
        this.mHasLongText = false;
    }

    /**
     * Constructor for creating a global update, with a summary of the long
     * text displayed when clicking on card.
     *
     * @param summary short summary of the long text
     * @param longText the entire update
     */
    public UpdateImpl(String summary, String longText) {
        this.mAffected = new GlobalAffected();
        this.mSummary = summary;
        this.mHasLongText = true;
        this.mLongText = longText;
    }

    /**
     * Creates an update with no specifc affection, and with only a short text.
     *
     * @param affects who this affects
     * @param text short text to display on card
     */
    public UpdateImpl(Affected affects, String text) {
        this.mAffected = affects;
        this.mSummary = text;
        this.mHasLongText = false;
    }

    public UpdateImpl(Affected affects, String summary, String longText) {
        this.mAffected = affects;
        this.mSummary = summary;
        this.mHasLongText = true;
        this.mLongText = longText;
    }

    @Override
    public Affected getAffected() {
        return mAffected;
    }

    @Override
    public String getText() {
        return mSummary;
    }

    @Override
    public boolean hasLongText() {
        return mHasLongText;
    }

    @Override
    public String getLongText() {
        return mLongText;
    }
}
