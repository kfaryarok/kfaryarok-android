package io.github.kfaryarok.kfaryarokapp;

import org.junit.Test;

import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ClassUtilUnitTest {

    /**
     * Tests {@link ClassUtil#convertEnglishClassToHebrew(String)}
     */
    @Test
    public void englishClassToHebrewConversion() {
        // null check
        assertThat(ClassUtil.convertEnglishClassToHebrew(null), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew(""), is(""));

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

        // invalid data
        assertThat(ClassUtil.convertEnglishClassToHebrew("L"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("LD"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("LDT"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("2"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("24"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("124"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("2F"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("22F"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("F3F"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("$"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("$!"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("$!@"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("F!@"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("F2@"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("F!3"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("#13"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("#1D"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("ז"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("יא"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("ז5"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("ז11"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("יא5"), is(""));
        assertThat(ClassUtil.convertEnglishClassToHebrew("יא15"), is(""));
    }

    /**
     * Tests {@link ClassUtil#convertHebrewClassToEnglish(String)}.
     * Checks every grade, each with a 1-digit class number and a 2-digits class number
     */
    @Test
    public void hebrewClassToEnglishConversion() {
        // null check
        assertThat(ClassUtil.convertHebrewClassToEnglish(null), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish(""), is(""));

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

        // invalid data
        assertThat(ClassUtil.convertHebrewClassToEnglish("ח"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("יא"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("יאב"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("2"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("24"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("124"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("2ח"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("22ט"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ב3א"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("$"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("$!"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("$!@"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ם!@"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("ו2@"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("נ!3"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("#13"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("#1ק"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("J"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("JI"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("L5"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("G11"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("YU5"), is(""));
        assertThat(ClassUtil.convertHebrewClassToEnglish("LF15"), is(""));
    }

    /**
     * Tests {@link ClassUtil#parseHebrewGrade(String)}
     */
    @Test
    public void hebrewGradeParsing() {
        // null check
        assertThat(ClassUtil.parseHebrewGrade(null), is(""));
        assertThat(ClassUtil.parseHebrewGrade(""), is(""));

        // grade G - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("ז3"), is("ז"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("ז11"), is("ז"));

        // grade H - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("ח2"), is("ח"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("ח10"), is("ח"));

        // grade I -  1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("ט7"), is("ט"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("ט13"), is("ט"));

        // grade J - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("י6"), is("י"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("י12"), is("י"));

        // grade K - 2 letters
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("יא1"), is("יא"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("יא12"), is("יא"));

        // grade L - 2 letters
        // single digit
        assertThat(ClassUtil.parseHebrewGrade("יב4"), is("יב"));
        // double digit
        assertThat(ClassUtil.parseHebrewGrade("יב11"), is("יב"));

        // invalid data
        assertThat(ClassUtil.parseHebrewGrade("h2"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("2"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("H"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("ע"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("י"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("?"), is(""));
        assertThat(ClassUtil.parseHebrewGrade("42"), is(""));
        // assertThat(ClassUtil.parseHebrewGrade("כ3"), is(""));
        // TODO look into perhaps making sure that the content matters, and not only formatting?
    }

    /**
     * Tests {@link ClassUtil#parseHebrewGrade(String)}
     */
    @Test
    public void hebrewClassNumberParsing() {
        // null check
        assertThat(ClassUtil.parseHebrewClassNumber(null), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber(""), is(0));

        // grade G - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("ז3"), is(3));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("ז11"), is(11));

        // grade H - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("ח2"), is(2));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("ח10"), is(10));

        // grade I -  1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("ט7"), is(7));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("ט13"), is(13));

        // grade J - 1 letter
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("י6"), is(6));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("י12"), is(12));

        // grade K - 2 letters
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("יא1"), is(1));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("יא12"), is(12));

        // grade L - 2 letters
        // single digit
        assertThat(ClassUtil.parseHebrewClassNumber("יב4"), is(4));
        // double digit
        assertThat(ClassUtil.parseHebrewClassNumber("יב11"), is(11));

        // invalid data
        assertThat(ClassUtil.parseHebrewClassNumber("h2"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("2"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("H"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("ע"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("י"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("?"), is(0));
        assertThat(ClassUtil.parseHebrewClassNumber("42"), is(0));
        // assertThat(ClassUtil.parseHebrewClassNumber("כ3"), is(0));
        // TODO look into perhaps making sure that the content matters, and not only formatting?
    }

    /**
     * Tests {@link ClassUtil#checkValidClassName(String)}
     * This test is essentially just a copy of {@link #validEnglishClassCheck()}
     * and {@link #validHebrewClassCheck()}, but running {@link ClassUtil#checkValidClassName(String)}
     * instead of their own methods.
     */
    @Test
    public void validClassCheck() {
        // ENGLISH
        // null check
        assertFalse(ClassUtil.checkValidEnglishClassName(null));
        assertFalse(ClassUtil.checkValidEnglishClassName(""));

        // grade G
        // single digit
        assertTrue(ClassUtil.checkValidClassName("G3"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("G11"));

        // grade H
        // single digit
        assertTrue(ClassUtil.checkValidClassName("H2"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("H10"));

        // grade I
        // single digit
        assertTrue(ClassUtil.checkValidClassName("I7"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("I13"));

        // grade J
        // single digit
        assertTrue(ClassUtil.checkValidClassName("J6"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("J12"));

        // grade K
        // single digit
        assertTrue(ClassUtil.checkValidClassName("K1"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("K12"));

        // grade L
        // single digit
        assertTrue(ClassUtil.checkValidClassName("L4"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("L11"));

        // invalid cases
        assertFalse(ClassUtil.checkValidClassName("F"));
        assertFalse(ClassUtil.checkValidClassName("FE"));
        assertFalse(ClassUtil.checkValidClassName("FEH"));
        assertFalse(ClassUtil.checkValidClassName("FEHO"));
        assertFalse(ClassUtil.checkValidClassName("1"));
        assertFalse(ClassUtil.checkValidClassName("14"));
        assertFalse(ClassUtil.checkValidClassName("143"));
        assertFalse(ClassUtil.checkValidClassName("1435"));
        assertFalse(ClassUtil.checkValidClassName("A234"));
        assertFalse(ClassUtil.checkValidClassName("AD2"));
        assertFalse(ClassUtil.checkValidClassName("ADC2"));
        assertFalse(ClassUtil.checkValidClassName("AD52"));

        // hebrew class check
        assertFalse(ClassUtil.checkValidEnglishClassName("ז5"));
        assertFalse(ClassUtil.checkValidEnglishClassName("ז12"));
        assertFalse(ClassUtil.checkValidEnglishClassName("יא1"));
        assertFalse(ClassUtil.checkValidEnglishClassName("יא11"));

        // HEBREW
        // null check
        assertFalse(ClassUtil.checkValidHebrewClassName(null));
        assertFalse(ClassUtil.checkValidHebrewClassName(""));

        // grade G
        // single digit
        assertTrue(ClassUtil.checkValidClassName("ז3"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("ז11"));

        // grade H
        // single digit
        assertTrue(ClassUtil.checkValidClassName("ח2"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("ח10"));

        // grade I
        // single digit
        assertTrue(ClassUtil.checkValidClassName("ט7"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("ט13"));

        // grade J
        // single digit
        assertTrue(ClassUtil.checkValidClassName("י6"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("י12"));

        // grade K
        // single digit
        assertTrue(ClassUtil.checkValidClassName("יא1"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("יא12"));

        // grade L
        // single digit
        assertTrue(ClassUtil.checkValidClassName("יב4"));
        // double digits
        assertTrue(ClassUtil.checkValidClassName("יב11"));

        // invalid cases
        assertFalse(ClassUtil.checkValidClassName("י"));
        assertFalse(ClassUtil.checkValidClassName("יא"));
        assertFalse(ClassUtil.checkValidClassName("יאה"));
        assertFalse(ClassUtil.checkValidClassName("יאהפ"));
        assertFalse(ClassUtil.checkValidClassName("1"));
        assertFalse(ClassUtil.checkValidClassName("12"));
        assertFalse(ClassUtil.checkValidClassName("124"));
        assertFalse(ClassUtil.checkValidClassName("1243"));
        assertFalse(ClassUtil.checkValidClassName("יאב2"));
        assertFalse(ClassUtil.checkValidClassName("י234"));

        // english class check
        assertFalse(ClassUtil.checkValidHebrewClassName("G2"));
        assertFalse(ClassUtil.checkValidHebrewClassName("G12"));
    }

    /**
     * Tests {@link ClassUtil#checkValidEnglishClassName(String)}
     */
    @Test
    public void validEnglishClassCheck() {
        // null check
        assertFalse(ClassUtil.checkValidEnglishClassName(null));
        assertFalse(ClassUtil.checkValidEnglishClassName(""));

        // grade G
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("G3"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("G11"));

        // grade H
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("H2"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("H10"));

        // grade I
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("I7"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("I13"));

        // grade J
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("J6"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("J12"));

        // grade K
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("K1"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("K12"));

        // grade L
        // single digit
        assertTrue(ClassUtil.checkValidEnglishClassName("L4"));
        // double digits
        assertTrue(ClassUtil.checkValidEnglishClassName("L11"));

        // invalid cases
        assertFalse(ClassUtil.checkValidEnglishClassName("F"));
        assertFalse(ClassUtil.checkValidEnglishClassName("FE"));
        assertFalse(ClassUtil.checkValidEnglishClassName("FEH"));
        assertFalse(ClassUtil.checkValidEnglishClassName("FEHO"));
        assertFalse(ClassUtil.checkValidEnglishClassName("1"));
        assertFalse(ClassUtil.checkValidEnglishClassName("14"));
        assertFalse(ClassUtil.checkValidEnglishClassName("143"));
        assertFalse(ClassUtil.checkValidEnglishClassName("1435"));
        assertFalse(ClassUtil.checkValidEnglishClassName("A234"));
        assertFalse(ClassUtil.checkValidEnglishClassName("AD2"));
        assertFalse(ClassUtil.checkValidEnglishClassName("ADC2"));
        assertFalse(ClassUtil.checkValidEnglishClassName("AD52"));

        // hebrew class check
        assertFalse(ClassUtil.checkValidEnglishClassName("ז5"));
        assertFalse(ClassUtil.checkValidEnglishClassName("ז12"));
        assertFalse(ClassUtil.checkValidEnglishClassName("יא1"));
        assertFalse(ClassUtil.checkValidEnglishClassName("יא11"));
    }

    /**
     * Tests {@link ClassUtil#checkValidHebrewClassName(String)}
     */
    @Test
    public void validHebrewClassCheck() {
        // null check
        assertFalse(ClassUtil.checkValidHebrewClassName(null));
        assertFalse(ClassUtil.checkValidHebrewClassName(""));

        // grade G
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("ז3"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("ז11"));

        // grade H
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("ח2"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("ח10"));

        // grade I
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("ט7"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("ט13"));

        // grade J
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("י6"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("י12"));

        // grade K
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("יא1"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("יא12"));

        // grade L
        // single digit
        assertTrue(ClassUtil.checkValidHebrewClassName("יב4"));
        // double digits
        assertTrue(ClassUtil.checkValidHebrewClassName("יב11"));
        
        // invalid cases
        assertFalse(ClassUtil.checkValidHebrewClassName("י"));
        assertFalse(ClassUtil.checkValidHebrewClassName("יא"));
        assertFalse(ClassUtil.checkValidHebrewClassName("יאה"));
        assertFalse(ClassUtil.checkValidHebrewClassName("יאהפ"));
        assertFalse(ClassUtil.checkValidHebrewClassName("1"));
        assertFalse(ClassUtil.checkValidHebrewClassName("12"));
        assertFalse(ClassUtil.checkValidHebrewClassName("124"));
        assertFalse(ClassUtil.checkValidHebrewClassName("1243"));
        assertFalse(ClassUtil.checkValidHebrewClassName("יאב2"));
        assertFalse(ClassUtil.checkValidHebrewClassName("י234"));

        // english class check
        assertFalse(ClassUtil.checkValidHebrewClassName("G2"));
        assertFalse(ClassUtil.checkValidHebrewClassName("G12"));
    }

    /**
     * Tests {@link ClassUtil#isHebrewLetter(char)}
     */
    @Test
    public void hebrewLetterCheck() {
        // go through every hebrew letter
        assertTrue(ClassUtil.isHebrewLetter('א'));
        assertTrue(ClassUtil.isHebrewLetter('ב'));
        assertTrue(ClassUtil.isHebrewLetter('ג'));
        assertTrue(ClassUtil.isHebrewLetter('ד'));
        assertTrue(ClassUtil.isHebrewLetter('ה'));
        assertTrue(ClassUtil.isHebrewLetter('ו'));
        assertTrue(ClassUtil.isHebrewLetter('ז'));
        assertTrue(ClassUtil.isHebrewLetter('ח'));
        assertTrue(ClassUtil.isHebrewLetter('ט'));
        assertTrue(ClassUtil.isHebrewLetter('י'));
        assertTrue(ClassUtil.isHebrewLetter('כ'));
        assertTrue(ClassUtil.isHebrewLetter('ל'));
        assertTrue(ClassUtil.isHebrewLetter('מ'));
        assertTrue(ClassUtil.isHebrewLetter('נ'));
        assertTrue(ClassUtil.isHebrewLetter('ס'));
        assertTrue(ClassUtil.isHebrewLetter('ע'));
        assertTrue(ClassUtil.isHebrewLetter('פ'));
        assertTrue(ClassUtil.isHebrewLetter('צ'));
        assertTrue(ClassUtil.isHebrewLetter('ק'));
        assertTrue(ClassUtil.isHebrewLetter('ר'));
        assertTrue(ClassUtil.isHebrewLetter('ש'));
        assertTrue(ClassUtil.isHebrewLetter('ת'));
        assertTrue(ClassUtil.isHebrewLetter('ן'));
        assertTrue(ClassUtil.isHebrewLetter('ף'));
        assertTrue(ClassUtil.isHebrewLetter('ך'));
        assertTrue(ClassUtil.isHebrewLetter('ץ'));
        assertTrue(ClassUtil.isHebrewLetter('ם'));

        // go through random non-hebrew chars
        assertFalse(ClassUtil.isHebrewLetter('f'));
        assertFalse(ClassUtil.isHebrewLetter('2'));
        assertFalse(ClassUtil.isHebrewLetter('$'));
        assertFalse(ClassUtil.isHebrewLetter('G'));
        assertFalse(ClassUtil.isHebrewLetter('!'));
        assertFalse(ClassUtil.isHebrewLetter('#'));
        assertFalse(ClassUtil.isHebrewLetter('f'));
        assertFalse(ClassUtil.isHebrewLetter('\''));
        assertFalse(ClassUtil.isHebrewLetter('"'));
        assertFalse(ClassUtil.isHebrewLetter('['));
        assertFalse(ClassUtil.isHebrewLetter('<'));
        assertFalse(ClassUtil.isHebrewLetter(' '));
    }

    /**
     * Tests {@link ClassUtil#convertEnglishGradeToHebrew(char)}
     */
    @Test
    public void englishGradeToHebrewConversion() {
        // null check
        assertThat(ClassUtil.convertEnglishGradeToHebrew(' '), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('2'), is("")); // random char

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

        // invalid data
        assertThat(ClassUtil.convertEnglishGradeToHebrew('A'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('a'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('כ'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('ז'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('ז'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('?'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('2'), is(""));
        assertThat(ClassUtil.convertEnglishGradeToHebrew('!'), is(""));
    }

    /**
     * Tests {@link ClassUtil#convertHebrewGradeToEnglish(String)}
     */
    @Test
    public void hebrewGradeToEnglishConversion() {
        // null check
        assertThat(ClassUtil.convertHebrewGradeToEnglish(null), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish(""), is(' '));

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

        // invalid data
        assertThat(ClassUtil.convertHebrewGradeToEnglish("h"), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish("H"), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish("כ"), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish("יג"), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish("2"), is(' '));
        assertThat(ClassUtil.convertHebrewGradeToEnglish("24"), is(' '));
    }

    /**
     * Tests {@link ClassUtil#isValidHebrewGrade(String)}
     */
    @Test
    public void validHebrewGradeCheck() {
        // null check
        assertFalse(ClassUtil.isValidHebrewGrade(null));
        assertFalse(ClassUtil.isValidHebrewGrade(""));

        // real data
        assertTrue(ClassUtil.isValidHebrewGrade("ז"));
        assertTrue(ClassUtil.isValidHebrewGrade("ח"));
        assertTrue(ClassUtil.isValidHebrewGrade("ט"));
        assertTrue(ClassUtil.isValidHebrewGrade("י"));
        assertTrue(ClassUtil.isValidHebrewGrade("יא"));
        assertTrue(ClassUtil.isValidHebrewGrade("יב"));

        // invalid data
        assertFalse(ClassUtil.isValidHebrewGrade("H"));
        assertFalse(ClassUtil.isValidHebrewGrade("i"));
        assertFalse(ClassUtil.isValidHebrewGrade("יג"));
        assertFalse(ClassUtil.isValidHebrewGrade("כ"));
        assertFalse(ClassUtil.isValidHebrewGrade("?"));
        assertFalse(ClassUtil.isValidHebrewGrade("24"));
        assertFalse(ClassUtil.isValidHebrewGrade("2"));
    }

}
