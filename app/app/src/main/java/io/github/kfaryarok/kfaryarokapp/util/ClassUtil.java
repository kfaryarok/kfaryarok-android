package io.github.kfaryarok.kfaryarokapp.util;

/**
 * Various utility methods for converting class names from English to Hebrew.
 * ie: I3 -> י3
 * (and for some reason android studio renders hebrew incorrectly in comments)
 *
 * @author tbsc on 03/03/2017
 */
public class ClassUtil {

    /**
     * Converts the class name from English to Hebrew.
     * Takes the English letter, converts to a Hebrew grade letter(s)
     * using {@link #convertEnglishGradeToHebrew(char)}, and appends the
     * class number.
     *
     * @param clazz English class name
     * @return Hebrew class name
     */
    public static String convertEnglishClassToHebrew(String clazz) {
        return (clazz == null || clazz.length() == 0) ? "" : convertEnglishGradeToHebrew(clazz.charAt(0)) + clazz.substring(1);
    }

    /**
     * Converts the class name from Hebrew to English using dark magic.
     *
     * @param clazz Hebrew class name
     * @return English class name
     */
    public static String convertHebrewClassToEnglish(String clazz) {
        StringBuilder sb = new StringBuilder();

        if (clazz == null || clazz.length() == 0) {
            // invalid, so return null
            return "";
        }

        if (clazz.length() == 2) {
            // class name is just 2 chars, so only 1 letter and 1 digit
            // append converted english grade
            sb.append(convertHebrewGradeToEnglish(String.valueOf(clazz.charAt(0))));
            // append class number
            sb.append(clazz.charAt(1));
        } else if (clazz.length() == 3) {
            // class name is 3 chars, so 2nd char can be either a letter of a digit
            // if 2nd char is digit then grade is only one letter
            if (Character.isDigit(clazz.charAt(1))) {
                // append converted english grade
                sb.append(convertHebrewGradeToEnglish(String.valueOf(clazz.charAt(0))));
                // append double-digit class number
                sb.append(clazz.substring(1));
            } else {
                // grade is 2 letters
                // convert grade in first 2 chars to english and append
                sb.append(convertHebrewGradeToEnglish(clazz.substring(0, 1)));
                // append single-digit class number
                sb.append(clazz.charAt(2));
            }
        } else {
            // double letter grade and double digit class num
            // convert grade in first 2 chars to english and append
            sb.append(convertHebrewGradeToEnglish(clazz.substring(0, 1)));
            // append double-digit class number
            sb.append(clazz.substring(2));
        }

        return sb.toString();
    }

    /**
     * Chceks if the string is a valid class name, in either English or Hebrew.
     * @param clazz string to check
     * @return is it valid
     */
    public static boolean checkValidClassName(String clazz) {
        return !(clazz == null || clazz.length() == 0) && (checkValidEnglishClassName(clazz) || checkValidHebrewClassName(clazz));
    }

    /**
     * Checks if the given string is a valid class name in English.
     * @param clazz The alleged class name string
     * @return is it really valid (in English)
     */
    public static boolean checkValidEnglishClassName(String clazz) {
        if (clazz == null || clazz.length() == 0) {
            return false;
        }

        // first char must always be a letter, so check it
        if ((clazz.charAt(0) <= 'a' && clazz.charAt(0) >= 'z') || (clazz.charAt(0) <= 'A' && clazz.charAt(0) >= 'Z')) {
            // i do it like this because i want to make sure its an english letter specifically
            // 1st char is not an english letter
            return false;
        }

        if (clazz.length() == 2) {
            if (!Character.isDigit(clazz.charAt(1))) {
                // 2nd char is not a digit, and it must be
                return false;
            }
        } else if (clazz.length() == 3) {
            if (!Character.isDigit(clazz.charAt(1))) {
                // 2nd char is not a digit
                return false;
            }
            if (!Character.isDigit(clazz.charAt(2))) {
                // 3nd char is not a digit
                return false;
            }
        } else {
            // invalid length, so i already know it's invalid
            return false;
        }

        // passed all checks
        return true;
    }

    /**
     * Checks if the string is a valid Hebrew class name.
     * @param clazz string to check
     * @return is valid hebrew class name
     */
    public static boolean checkValidHebrewClassName(String clazz) {
        if (clazz == null || clazz.length() == 0) {
            return false;
        }

        // first char has be a letter, so check it once
        if (!isHebrewLetter(clazz.charAt(0))) {
            return false;
        }

        if (clazz.length() == 2) {
            if (!Character.isDigit(clazz.charAt(1))) {
                // 2nd char must be a digit
                return false;
            }
        } else if (clazz.length() == 3) {
            if (isHebrewLetter(clazz.charAt(1))) {
                // 2nd char is a letter
                if (!Character.isDigit(clazz.charAt(2))) {
                    // then 3rd must be digit
                    return false;
                }
            } else {
                // 2nd char isn't a letter
                if (!Character.isDigit(clazz.charAt(1)) || !Character.isDigit(clazz.charAt(2))) {
                    // so 2nd and 3rd must be digits
                    return false;
                }
            }
        } else if (clazz.length() == 4) {
            if (!isHebrewLetter(clazz.charAt(1))) {
                // 2nd chars must be a letter
                return false;
            }
            if (!Character.isDigit(clazz.charAt(2)) || !Character.isDigit(clazz.charAt(3))) {
                // 3rd and 4th chars must be digits
                return false;
            }
        } else {
            // invalid length
            return false;
        }

        // passed all checks
        return true;
    }

    /**
     * Checks if the character is a letter in Hebrew.
     * @param letter the char to check
     * @return is hebrew letter
     */
    public static boolean isHebrewLetter(char letter) {
        // if letter is in range of the hebrew alphabet, in unicode formatting
        return letter >= 'א' && letter <= 'ת';
    }

    /**
     * Converts grades (G-L) from English to Hebrew, ignoring case.
     * Return value must be a string because grades can be more than 1 letter
     * @param englishGrade Grade in English
     * @return Grade in Hebrew (null if invalid/unsupported)
     */
    public static String convertEnglishGradeToHebrew(char englishGrade) {
        switch (englishGrade) {
            case 'G':
            case 'g':
                return "ז";
            case 'H':
            case 'h':
                return "ח";
            case 'I':
            case 'i':
                return "ט";
            case 'J':
            case 'j':
                return "י";
            case 'K':
            case 'k':
                return "יא";
            case 'L':
            case 'l':
                return "יב";
            default:
                return "";
        }
    }

    /**
     * Converts grades (7-12) from Hebrew to English, capitalized.
     * @param hebrewGrade Grade in Hebrew
     * @return Grade in English, space ( ) if invalid/unsupported
     */
    public static char convertHebrewGradeToEnglish(String hebrewGrade) {
        if (hebrewGrade == null || hebrewGrade.length() == 0) {
            return ' ';
        }

        switch (hebrewGrade) {
            case "ז":
                return 'G';
            case "ח":
                return 'H';
            case "ט":
                return 'I';
            case "י":
                return 'J';
            case "יא":
                return 'K';
            case "יב":
                return 'L';
            default:
                return ' ';
        }
    }

}
