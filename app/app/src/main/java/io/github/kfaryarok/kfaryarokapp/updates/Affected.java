package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * Interface for checking is a certain class if affected.
 *
 * Created by tbsc on 03/03/2017.
 */
public interface Affected {

    /**
     * Check if this affects the given class.
     * @param clazz The class to check
     * @return if this affects that class
     */
    boolean affects(String clazz);

    /**
     * Only if the update is global should this be returning true.
     * @return if update is global
     */
    boolean isGlobal();

}
