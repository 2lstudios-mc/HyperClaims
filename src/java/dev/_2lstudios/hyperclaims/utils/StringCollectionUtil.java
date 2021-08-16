package dev._2lstudios.hyperclaims.utils;

import java.util.Collection;

public class StringCollectionUtil {
    public static boolean containsEquals(final Collection<String> strings, final String string) {
        for (final String string2 : strings) {
            if (string.equals(string2)) {
                return true;
            }
        }
        return false;
    }
}
