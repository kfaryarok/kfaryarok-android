package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * Wrapper for global affections. Affects all classes and is global.
 *
 * @author tbsc on 03/03/2017
 */
public class GlobalAffected implements Affected {

    /**
     * Since it's a global update, it affects all classes.
     * @param clazz The class to check
     * @return true
     */
    @Override
    public boolean affects(String clazz) {
        return true;
    }

    /**
     * If this update is global. Must be true if it's here.
     * @return true
     */
    @Override
    public boolean isGlobal() {
        return true;
    }

}
