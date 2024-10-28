package org.infra.cqrs.validation;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

public class Guard {
    private static final UUID emptyUUID = new UUID(0, 0);

    public static void minLength(String source, int minLength, String paramName) {
        if (source.length() < minLength) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Input {0} can not have a length less than {1}.", paramName, minLength));
        }
    }

    public static void maxLength(String source, int maxLength, String paramName) {
        if (source.length() > maxLength)
        {
            throw new IllegalArgumentException(
                    MessageFormat.format("Input {0} can not have a length more than {1}.", paramName, maxLength));
        }
    }

    public static void positive(int source, String name) {
        positive((long)source, name);
    }

    public static void positive(long source, String name) {
        if (source <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} must be greater than 0", name));
        }
    }

    public static void positive(double source, String name) {
        if (source <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} must be greater than 0", name));
        }
    }

    public static <T> boolean containsElement(Iterable<T> source, String name) {
        return source.iterator().hasNext();
    }

    public static void notNullOrEmpty(String source, String name) {
        notNull(source, name);
        notEmpty(source, name);
    }

    public static void notNullOrDefault(Integer source, String name) {
        if (source == null || source == 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} can not be null or 0", name));
        }
    }

    public static void notNullOrDefault(Long source, String name) {
        if (source == null || source == 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} can not be null or 0", name));
        }
    }

    public static void notNullOrDefault(UUID source, String name) {
        if (source == null || emptyUUID.equals(source)) {
            throw new IllegalArgumentException(MessageFormat.format("{0} can not be null or empty", name));
        }
    }

    public static void notNull(Object source, String name) {
        if (source == null) {
            throw new IllegalArgumentException(
                    MessageFormat.format("{0} can not be null", name));
        }
    }

    public static void notEmpty(String source, String name) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format("{0} can not be empty", name));
        }
    }

    public static void notEmpty(UUID source, String name) {
        if (source == Guard.emptyUUID) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "{0} can not be empty", name
            ));
        }
    }

    public static void validPersian(String source, String name) {
        notNullOrEmpty(source, name);

        var isValid = match(source, "^([\u0600-\u06FF]+\s?)+$");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} has to be persian", name));
        }
    }

    public static void validMobile(String source, String name) {
        notNullOrEmpty(source, name);

        var isValid = match(source, "09[0-9]{9}");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} has to be valid mobile", name));
        }
    }

    public static void validNumber(String source, String name) {
        notNullOrEmpty(source, name);

        var isValid = match(source, "^[0-9]+$");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} has to be valid number", name));
        }
    }

    public static void validPhone(String source, String name) {
        notNullOrEmpty(source, name);

        // 91 27 46 97 66       --> 10 digits, no prefix or country code
        // 0 91 27 46 97 66     --> 11 digits, no prefix or country code
        // 98 91 27 46 97 66    --> 12 digits, with country code and no 00
        // + 98 91 27 46 97 66  --> 13 digits with + and country code prefix
        // 00 98 91 27 46 97 66 --> 14 digits with 00 country code prefix

        switch (source.length()) {
            case 10, 12 -> {
                validNumber(source, name);
                if (source.charAt(0) == '0')
                    throw new IllegalArgumentException("Invalid phone");
            }
            case 11 -> {
                validNumber(source, name);
                if (source.charAt(0) != '0')
                    throw new IllegalArgumentException("Invalid phone");
            }
            case 13 -> {
                if (source.charAt(0) != '+')
                    throw new IllegalArgumentException("Invalid phone");
            }
            case 14 -> validNumber(source, name);
            default -> throw new IllegalArgumentException("Invalid phone");
        }
    }

    public static void validEmail(String source, String name) {
        notNullOrEmpty(source, name);

        var isValid = match(source, "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is invalid email", name));
        }
    }

    public static void validDomain(String source, String name) {
        notNullOrEmpty(source, name);

        var isValid = match(source, "(localhost)|(http[s]?:\\/\\/|[a-z]*\\.[a-z]{3}\\.[a-z]{2})([a-z]*\\.[a-z]{3})|([a-z]*\\.[a-z]*\\.[a-z]{3}\\.[a-z]{2})|([a-z]+\\.[a-z]{3})");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is invalid email", name));
        }
    }

    public static void validNationalCode(String source, String name) {
        validNumber(source, name);

        var isValid = match(source, "[0-9]{10}");
        if (!isValid) {
            throw new IllegalArgumentException("invalid national code");
        }

        var allDigitEqual = new String [] { "0000000000", "1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999" };
        isValid = Arrays.asList(allDigitEqual).contains(source);
        if (isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} invalid national code", name));
        }

        var num1 = (int) source.charAt(0) * 10;
        var num2 = (int) source.charAt(1) * 9;
        var num3 = (int) source.charAt(2) * 8;
        var num4 = (int) source.charAt(3) * 7;
        var num5 = (int) source.charAt(4) * 6;
        var num6 = (int) source.charAt(5) * 5;
        var num7 = (int) source.charAt(6) * 4;
        var num8 = (int) source.charAt(7) * 3;
        var num9 = (int) source.charAt(8) * 2;
        var lastNumber = (int) source.charAt(9);

        var sum = num1 + num2 + num3 + num4 + num5 + num6 + num7 + num8 + num9;
        var result = sum % 11;

        isValid = (((result < 2) && (lastNumber == result)) || ((result >= 2) && ((11 - result) == lastNumber)));
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is invalid national code", name));
        }
    }

    public static void onlyAlphaNumeric(String source, String name) {
        notNullOrEmpty(source, name);
        var isValid = match(source.trim(), "^([a-zA-Z0-9\u0600-\u06FF]+\s*)*$");
        if (!isValid) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not alphanumeric", name));
        }
    }

    public static Duration getDefaultMatchTimeoutSeconds() {
        return Duration.ofSeconds(1);
    }

    private static boolean match(String text, String pattern) {
        return Match(text, pattern, getDefaultMatchTimeoutSeconds());
    }

    private static boolean Match(String text, String pattern, Duration duration) {
        try {
            return RegularExpressionUtils.createMatcherWithTimeout(text, pattern, (int)duration.toMillis()).matches();
        }
        catch (Exception ex) {
            return false;
        }
    }
}

