package io.github.kfaryarok.kfaryarokapp;

import org.junit.Test;

import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UpdateUnitTest {

    /**
     * Checks if {@link UpdateHelper#formatClassString(String[], String)} works as it should,
     * as in checking if it puts everything in the right order and with correct commas
     */
    @Test
    public void testClassStringFormatting() {
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ז4", "ח3", "י9"
        }, "י9"), is("י9, ז4, ח3"));

        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח3", "ז4", "י9"
        }, "י9"), is("י9, ח3, ז4"));

        assertThat(UpdateHelper.formatClassString(new String[] {
                "י9", "ח4", "ח3"
        }, "ח4"), is("ח4, י9, ח3"));
    }

}