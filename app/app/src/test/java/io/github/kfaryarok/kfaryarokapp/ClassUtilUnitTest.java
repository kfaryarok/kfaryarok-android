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
        // grade G
        assertThat(ClassUtil.convertHebrewGradeToEnglish("ז"), is('G'));

        // grade H
        assertThat(ClassUtil.convertHebrewGradeToEnglish("ח"), is('H'));

        // grade I
        assertThat(ClassUtil.convertHebrewGradeToEnglish("ט"), is('I'));

        // grade J
        assertThat(ClassUtil.convertHebrewGradeToEnglish("י"), is('J'));

        // grade K
        assertThat(ClassUtil.convertHebrewGradeToEnglish("יא"), is('K'));

        // grade L
        assertThat(ClassUtil.convertHebrewGradeToEnglish("יב"), is('L'));
    }

    @Test
    public void correctTranslationEnglishClassToHebrew() {
        // grade G
        assertThat(ClassUtil.convertEnglishClassToHebrew("G3"), is("ז3"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("G11"), is("ז11"));

        // grade H
        assertThat(ClassUtil.convertEnglishClassToHebrew("H4"), is("ח4"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("H10"), is("ח10"));

        // grade I
        assertThat(ClassUtil.convertEnglishClassToHebrew("I5"), is("ט5"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("I12"), is("ט12"));

        // grade J
        assertThat(ClassUtil.convertEnglishClassToHebrew("J2"), is("י2"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("J11"), is("י11"));

        // grade K
        assertThat(ClassUtil.convertEnglishClassToHebrew("K2"), is("יא2"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("K10"), is("יא10"));

        // grade L
        assertThat(ClassUtil.convertEnglishClassToHebrew("L2"), is("יב2"));
        assertThat(ClassUtil.convertEnglishClassToHebrew("L12"), is("יב12"));
    }

    @Test
    public void correctTranslationEnglishGradeToHebrew() {
        // grade G
        assertThat(ClassUtil.convertEnglishGradeToHebrew('G'), is("ז"));

        // grade H
        assertThat(ClassUtil.convertEnglishGradeToHebrew('H'), is("ח"));

        // grade I
        assertThat(ClassUtil.convertEnglishGradeToHebrew('I'), is("ט"));

        // grade J
        assertThat(ClassUtil.convertEnglishGradeToHebrew('J'), is("י"));

        // grade K
        assertThat(ClassUtil.convertEnglishGradeToHebrew('K'), is("יא"));

        // grade L
        assertThat(ClassUtil.convertEnglishGradeToHebrew('L'), is("יב"));
    }

}
