package io.github.kfaryarok.kfaryarokapp;

import org.junit.Test;

import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ClassUtilUnitTest {

    /**
     * Tests {@link ClassUtil#convertHebrewClassToEnglish(String)}.
     * Checks every grade, each with a 1-digit class number and a 2-digits class number
     */
    @Test
    public void correctTranslationHebrewClassToEnglish() {
        // grade G
        assertThat(ClassUtil.convertHebrewClassToEnglish("ז3"), is("G3"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ז11"), is("G11"));

        // grade H
        assertThat(ClassUtil.convertHebrewClassToEnglish("ח4"), is("H4"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ח10"), is("H10"));

        // grade I
        assertThat(ClassUtil.convertHebrewClassToEnglish("ט5"), is("I5"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ט12"), is("I12"));

        // grade J
        assertThat(ClassUtil.convertHebrewClassToEnglish("י2"), is("J2"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("י11"), is("J11"));

        // grade K
        assertThat(ClassUtil.convertHebrewClassToEnglish("יא2"), is("K2"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("יא10"), is("K10"));

        // grade L
        assertThat(ClassUtil.convertHebrewClassToEnglish("יב2"), is("L2"));
        assertThat(ClassUtil.convertHebrewClassToEnglish("יב12"), is("L12"));
    }

    @Test
    public void correctTranslationHebrewGradeToEnglish() {

    }

    @Test
    public void correctTranslationEnglishClassToHebrew() {

    }

    @Test
    public void correctTranslationEnglishGradeToHebrew() {

    }

}
