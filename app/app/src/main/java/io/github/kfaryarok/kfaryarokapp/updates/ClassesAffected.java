package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * Simple wrapper for affecting specific classes.
 * The classes need to be in English, and only when displayed to the user should
 * they be converted back to Hebrew.
 *
 * @author tbsc on 03/03/2017
 */
public class ClassesAffected implements Affected {

    private String[] mClassesAffected;

    public ClassesAffected(String... classes) {
        this.mClassesAffected = classes;
    }

    /**
     * If ever needed, gets the affected classes array.
     * Not in {@link Affected} because not every class has one.
     * @return affected classes array.
     */
    public String[] getClassesAffected() {
        return mClassesAffected;
    }

    /**
     * Checks to see if the class is affected, by seeing if it exists in the array of affected classes.
     * @param clazz The class to check
     * @return is this class affected
     */
    @Override
    public boolean affects(String clazz) {
        // daily null check
        if (clazz == null || clazz.length() == 0) {
            return false;
        }

        // loop through array
        for (String clazzAffected : mClassesAffected) {
            if (clazzAffected.equals(clazz)) {
                // exists
                return true;
            }
        }

        // doesn't
        return false;
    }

    /**
     * Isn't {@link GlobalAffected} or a subclass of it, so not global.
     * @return false
     */
    @Override
    public boolean isGlobal() {
        return false;
    }

}
