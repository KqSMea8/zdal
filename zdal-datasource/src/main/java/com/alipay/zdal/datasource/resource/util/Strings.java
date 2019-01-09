/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * A collection of String utilities.
 *
 * 
 * @author 伯牙
 * @version $Id: Strings.java, v 0.1 2014-1-6 下午05:40:14 Exp $
 */
public final class Strings {
    /** An empty string constant */
    public static final String    EMPTY            = "";

    /** Millisecond conversion constants */
    private static final long     MSEC             = 1;
    private static final long     SECS             = 1000;
    private static final long     MINS             = 60 * 1000;
    private static final long     HOUR             = 60 * 60 * 1000;

    /**
     * List of valid Java keywords, see The Java Language Specification
     * Second Edition Section 3.9, 3.10.3 and 3.10.7
     */
    private static final String[] keywords         = { "abstract", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const", "continue", "default", "do", "double",
            "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
            "volatile", "while",

            "true", // technically no keywords but we are not picky
            "false", "null"                       };

    /**
     * List of EJB-QL Identifiers as defined in the EJB 2.0 Specification
     * Section 11.2.6.1
     */
    private static final String[] ejbQlIdentifiers = { "AND", "AS", "BETWEEN", "DISTINCT", "EMPTY",
            "FALSE", "FROM", "IN", "IS", "LIKE", "MEMBER", "NOT", "NULL", "OBJECT", "OF", "OR",
            "SELECT", "UNKNOWN", "TRUE", "WHERE", };

    /////////////////////////////////////////////////////////////////////////
    //                         Substitution Methods                        //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Substitute sub-strings in side of a string.
     *
     * @param buff    Stirng buffer to use for substitution (buffer is not reset)
     * @param from    String to substitute from
     * @param to      String to substitute to
     * @param string  String to look for from in
     * @return        Substituted string
     */
    public static String subst(final StringBuffer buff, final String from, final String to,
                               final String string) {
        int begin = 0, end = 0;

        while ((end = string.indexOf(from, end)) != -1) {
            // append the first part of the string
            buff.append(string.substring(begin, end));

            // append the replaced string
            buff.append(to);

            // update positions
            begin = end + from.length();
            end = begin;
        }

        // append the rest of the string
        buff.append(string.substring(begin, string.length()));

        return buff.toString();
    }

    /**
     * Substitute sub-strings in side of a string.
     *
     * @param from    String to substitute from
     * @param to      String to substitute to
     * @param string  String to look for from in
     * @return        Substituted string
     */
    public static String subst(final String from, final String to, final String string) {
        return subst(new StringBuffer(), from, to, string);
    }

    /**
     * Substitute sub-strings in side of a string.
     *
     * @param buff       String buffer to use for substitution (buffer is not reset)
     * @param string     String to subst mappings in
     * @param map        Map of from->to strings
     * @param beginToken Beginning token
     * @param endToken   Ending token
     * @return           Substituted string
     */
    public static String subst(final StringBuffer buff, final String string, final Map map,
                               final String beginToken, final String endToken) {
        int begin = 0, rangeEnd = 0;
        Range range;

        while ((range = rangeOf(beginToken, endToken, string, rangeEnd)) != null) {
            // append the first part of the string
            buff.append(string.substring(begin, range.begin));

            // Get the string to replace from the map
            String key = string.substring(range.begin + beginToken.length(), range.end);
            Object value = map.get(key);
            // if mapping does not exist then use empty;
            if (value == null)
                value = EMPTY;

            // append the replaced string
            buff.append(value);

            // update positions
            begin = range.end + endToken.length();
            rangeEnd = begin;
        }

        // append the rest of the string
        buff.append(string.substring(begin, string.length()));

        return buff.toString();
    }

    /**
     * Substitute sub-strings in side of a string.
     *
     * @param string     String to subst mappings in
     * @param map        Map of from->to strings
     * @param beginToken Beginning token
     * @param endToken   Ending token
     * @return           Substituted string
     */
    public static String subst(final String string, final Map map, final String beginToken,
                               final String endToken) {
        return subst(new StringBuffer(), string, map, beginToken, endToken);
    }

    /**
     * Substitute index identifiers with the replacement value from the
     * given array for the corresponding index.
     *
     * @param buff       The string buffer used for the substitution
     *                   (buffer is not reset).
     * @param string     String substitution format.
     * @param replace    Array of strings whose values will be used as 
     *                   replacements in the given string when a token with
     *                   their index is found.
     * @param token      The character token to specify the start of an index
     *                   reference.
     * @return           Substituted string.
     */
    public static String subst(final StringBuffer buff, final String string,
                               final String replace[], final char token) {
        int i = string.length();
        for (int j = 0; j >= 0 && j < i; j++) {
            char c = string.charAt(j);

            // if the char is the token, then get the index
            if (c == token) {

                // if we aren't at the end of the string, get the index
                if (j != i) {
                    int k = Character.digit(string.charAt(j + 1), 10);

                    if (k == -1) {
                        buff.append(string.charAt(j + 1));
                    } else if (k < replace.length) {
                        buff.append(replace[k]);
                    }

                    j++;
                }
            } else {
                buff.append(c);
            }
        }

        return buff.toString();
    }

    /**
     * Substitute index identifiers with the replacement value from the
     * given array for the corresponding index.
     *
     * @param string     String substitution format.
     * @param replace    Array of strings whose values will be used as 
     *                   replacements in the given string when a token with
     *                   their index is found.
     * @param token      The character token to specify the start of an index
     *                   reference.
     * @return           Substituted string.
     */
    public static String subst(final String string, final String replace[], final char token) {
        return subst(new StringBuffer(), string, replace, token);
    }

    /**
     * Substitute index identifiers (with <code>%</code> for the index token)
     * with the replacement value from the given array for the corresponding
     * index.
     *
     * @param string     String substitution format.
     * @param replace    Array of strings whose values will be used as 
     *                   replacements in the given string when a token with
     *                   their index is found.
     * @return           Substituted string.
     */
    public static String subst(final String string, final String replace[]) {
        return subst(new StringBuffer(), string, replace, '%');
    }

    /////////////////////////////////////////////////////////////////////////
    //                             Range Methods                           //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Represents a range between two integers.
     */
    public static class Range {
        /** The beginning of the range. */
        public int begin;

        /** The end of the range. */
        public int end;

        /**
         * Construct a new range.
         *
         * @param begin   The beginning of the range.
         * @param end     The end of the range.
         */
        public Range(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        /**
         * Default constructor.
         */
        public Range() {
        }
    }

    /**
     * Return the range from a begining token to an ending token.
     *
     * @param beginToken String to indicate begining of range.
     * @param endToken   String to indicate ending of range.
     * @param string     String to look for range in.
     * @param fromIndex  Beginning index.
     * @return           (begin index, end index) or <i>null</i>.
     */
    public static Range rangeOf(final String beginToken, final String endToken,
                                final String string, final int fromIndex) {
        int begin = string.indexOf(beginToken, fromIndex);

        if (begin != -1) {
            int end = string.indexOf(endToken, begin + 1);
            if (end != -1) {
                return new Range(begin, end);
            }
        }

        return null;
    }

    /**
     * Return the range from a begining token to an ending token.
     *
     * @param beginToken String to indicate begining of range.
     * @param endToken   String to indicate ending of range.
     * @param string     String to look for range in.
     * @return           (begin index, end index) or <i>null</i>.
     */
    public static Range rangeOf(final String beginToken, final String endToken, final String string) {
        return rangeOf(beginToken, endToken, string, 0);
    }

    /////////////////////////////////////////////////////////////////////////
    //                           Spliting Methods                          //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Split up a string into multiple strings based on a delimiter.
     *
     * @param string  String to split up.
     * @param delim   Delimiter.
     * @param limit   Limit the number of strings to split into
     *                (-1 for no limit).
     * @return        Array of strings.
     */
    public static String[] split(final String string, final String delim, final int limit) {
        // get the count of delim in string, if count is > limit 
        // then use limit for count.  The number of delimiters is less by one
        // than the number of elements, so add one to count.
        int count = count(string, delim) + 1;
        if (limit > 0 && count > limit) {
            count = limit;
        }

        String strings[] = new String[count];
        int begin = 0;

        for (int i = 0; i < count; i++) {
            // get the next index of delim
            int end = string.indexOf(delim, begin);

            // if the end index is -1 or if this is the last element
            // then use the string's length for the end index
            if (end == -1 || i + 1 == count)
                end = string.length();

            // if end is 0, then the first element is empty
            if (end == 0)
                strings[i] = EMPTY;
            else
                strings[i] = string.substring(begin, end);

            // update the begining index
            begin = end + 1;
        }

        return strings;
    }

    /**
     * Split up a string into multiple strings based on a delimiter.
     *
     * @param string  String to split up.
     * @param delim   Delimiter.
     * @return        Array of strings.
     */
    public static String[] split(final String string, final String delim) {
        return split(string, delim, -1);
    }

    /////////////////////////////////////////////////////////////////////////
    //                    Joining/Concatenation Methods                    //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Join an array of strings into one delimited string.
     *
     * @param buff    String buffered used for join (buffer is not reset).
     * @param array   Array of objects to join as strings.
     * @param delim   Delimiter to join strings with or <i>null</i>.
     * @return        Joined string.
     */
    public static String join(final StringBuffer buff, final Object array[], final String delim) {
        boolean haveDelim = (delim != null);

        for (int i = 0; i < array.length; i++) {
            buff.append(array[i]);

            // if this is the last element then don't append delim
            if (haveDelim && (i + 1) < array.length) {
                buff.append(delim);
            }
        }

        return buff.toString();
    }

    /**
     * Join an array of strings into one delimited string.
     *
     * @param array   Array of objects to join as strings.
     * @param delim   Delimiter to join strings with or <i>null</i>.
     * @return        Joined string.
     */
    public static String join(final Object array[], final String delim) {
        return join(new StringBuffer(), array, delim);
    }

    /**
     * Convert and join an array of objects into one string.
     *
     * @param array   Array of objects to join as strings.
     * @return        Converted and joined objects.
     */
    public static String join(final Object array[]) {
        return join(array, null);
    }

    /**
     * Convert and join an array of bytes into one string.
     *
     * @param array   Array of objects to join as strings.
     * @return        Converted and joined objects.
     */
    public static String join(final byte array[]) {
        Byte bytes[] = new Byte[array.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new Byte(array[i]);
        }

        return join(bytes, null);
    }

    /**
     * Return a string composed of the given array.
     *
     * @param buff       Buffer used to construct string value (not reset).
     * @param array      Array of objects.
     * @param prefix     String prefix.
     * @param separator  Element sepearator.
     * @param suffix     String suffix.
     * @return           String in the format of:
     *                   prefix + n ( + separator + n+i)* + suffix.
     */
    public static String join(final StringBuffer buff, final Object[] array, final String prefix,
                              final String separator, final String suffix) {
        buff.append(prefix);
        join(buff, array, separator);
        buff.append(suffix);

        return buff.toString();
    }

    /**
     * Return a string composed of the given array.
     *
     * @param array      Array of objects.
     * @param prefix     String prefix.
     * @param separator  Element sepearator.
     * @param suffix     String suffix.
     * @return           String in the format of:
     *                   prefix + n ( + separator + n+i)* + suffix.
     */
    public static String join(final Object[] array, final String prefix, final String separator,
                              final String suffix) {
        return join(new StringBuffer(), array, prefix, separator, suffix);
    }

    /////////////////////////////////////////////////////////////////////////
    //                          Counting Methods                           //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Count the number of instances of substring within a string.
     *
     * @param string     String to look for substring in.
     * @param substring  Sub-string to look for.
     * @return           Count of substrings in string.
     */
    public static int count(final String string, final String substring) {
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1) {
            idx++;
            count++;
        }

        return count;
    }

    /**
     * Count the number of instances of character within a string.
     *
     * @param string     String to look for substring in.
     * @param c          Character to look for.
     * @return           Count of substrings in string.
     */
    public static int count(final String string, final char c) {
        return count(string, String.valueOf(c));
    }

    /////////////////////////////////////////////////////////////////////////
    //                            Padding Methods                          //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Return a string padded with the given string for the given count.
     *
     * @param buff       String buffer used for padding (buffer is not reset).
     * @param string     Pad element.
     * @param count      Pad count.
     * @return           Padded string.
     */
    public static String pad(final StringBuffer buff, final String string, final int count) {
        for (int i = 0; i < count; i++) {
            buff.append(string);
        }

        return buff.toString();
    }

    /**
     * Return a string padded with the given string for the given count.
     *
     * @param string     Pad element.
     * @param count      Pad count.
     * @return           Padded string.
     */
    public static String pad(final String string, final int count) {
        return pad(new StringBuffer(), string, count);
    }

    /**
     * Return a string padded with the given string value of an object
     * for the given count.
     *
     * @param obj     Object to convert to a string.
     * @param count   Pad count.
     * @return        Padded string.
     */
    public static String pad(final Object obj, final int count) {
        return pad(new StringBuffer(), String.valueOf(obj), count);
    }

    /////////////////////////////////////////////////////////////////////////
    //                              Misc Methods                           //
    /////////////////////////////////////////////////////////////////////////

    /**
     * <p>Compare two strings.
     *
     * <p>Both or one of them may be null.
     *
     * @return true if object equals or intern ==, else false. 
     */
    public static boolean compare(final String me, final String you) {
        // If both null or intern equals
        if (me == you)
            return true;

        // if me null and you are not
        if (me == null && you != null)
            return false;

        // me will not be null, test for equality
        return me.equals(you);
    }

    /**
     * Check if the given string is empty.
     *
     * @param string     String to check
     * @return           True if string is empty
     */
    public static boolean isEmpty(final String string) {
        return string.equals(EMPTY);
    }

    /**
     * Return the <i>nth</i> index of the given token occurring in the given string.
     *
     * @param string     String to search.
     * @param token      Token to match.
     * @param index      <i>Nth</i> index.
     * @return           Index of <i>nth</i> item or -1.
     */
    public static int nthIndexOf(final String string, final String token, final int index) {
        int j = 0;

        for (int i = 0; i < index; i++) {
            j = string.indexOf(token, j + 1);
            if (j == -1)
                break;
        }

        return j;
    }

    /**
     * Capitalize the first character of the given string.
     *
     * @param string     String to capitalize.
     * @return           Capitalized string.
     *
     * @throws IllegalArgumentException    String is <kk>null</kk> or empty.
     */
    public static String capitalize(final String string) {
        if (string == null)
            throw new NullArgumentException("string");
        if (string.equals(""))
            throw new EmptyStringException("string");

        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    /**
     * Trim each string in the given string array.
     *
     * <p>This modifies the string array.
     *
     * @param strings    String array to trim.
     * @return           String array with each element trimmed.
     */
    public static String[] trim(final String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
        }

        return strings;
    }

    /**
     * Make a URL from the given string.
     *
     * <p>
     * If the string is a properly formatted file URL, then the file
     * portion will be made canonical.
     *
     * <p>
     * If the string is an invalid URL then it will be converted into a
     * file URL.
     *
     * @param urlspec           The string to construct a URL for.
     * @param relativePrefix    The string to prepend to relative file
     *                          paths, or null to disable prepending.
     * @return                  A URL for the given string.
     *
     * @throws MalformedURLException  Could not make a URL for the given string.
     */
    public static URL toURL(String urlspec, final String relativePrefix)
                                                                        throws MalformedURLException {
        urlspec = urlspec.trim();

        URL url;

        try {
            url = new URL(urlspec);
            if (url.getProtocol().equals("file")) {
                url = makeURLFromFilespec(url.getFile(), relativePrefix);
            }
        } catch (Exception e) {
            // make sure we have a absolute & canonical file url
            try {
                url = makeURLFromFilespec(urlspec, relativePrefix);
            } catch (IOException n) {
                //
                // jason: or should we rethrow e?
                //
                throw new MalformedURLException(n.toString());
            }
        }

        return url;
    }

    /** A helper to make a URL from a filespec. */
    private static URL makeURLFromFilespec(final String filespec, final String relativePrefix)
                                                                                              throws IOException {
        // make sure the file is absolute & canonical file url
        File file = new File(filespec);

        // if we have a prefix and the file is not abs then prepend
        if (relativePrefix != null && !file.isAbsolute()) {
            file = new File(relativePrefix, filespec);
        }

        // make sure it is canonical (no ../ and such)
        file = file.getCanonicalFile();

        return file.toURL();
    }

    /**
     * Make a URL from the given string.
     *
     * @see #toURL(String,String)
     *
     * @param urlspec    The string to construct a URL for.
     * @return           A URL for the given string.
     *
     * @throws MalformedURLException  Could not make a URL for the given string.
     */
    public static URL toURL(final String urlspec) throws MalformedURLException {
        return toURL(urlspec, null);
    }

    /**
     * Check whether the given String is a reserved Java Keyword
     * according to the Java Language Specifications.
     *
     * @param s String to check
     *
     * @return <code>true</code> if the given String is a reserved Java
     *         keyword, <code>false</code> otherwise.
     */
    public final static boolean isJavaKeyword(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether the given String is an identifier according to the
     * EJB-QL definition. See The EJB 2.0 Documentation Section 11.2.6.1.
     *
     * @param s String to check
     *
     * @return <code>true</code> if the given String is a reserved
     *         identifier in EJB-QL, <code>false</code> otherwise.
     */
    public final static boolean isEjbQlIdentifier(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        for (int i = 0; i < ejbQlIdentifiers.length; i++) {
            if (ejbQlIdentifiers[i].equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether the given String is a valid identifier according
     * to the Java Language specifications.
     *
     * See The Java Language Specification Second Edition, Section 3.8
     * for the definition of what is a valid identifier.
     *
     * @param s String to check
     *
     * @return <code>true</code> if the given String is a valid Java
     *         identifier, <code>false</code> otherwise.
     */
    public final static boolean isValidJavaIdentifier(String s) {
        // an empty or null string cannot be a valid identifier
        if (s == null || s.length() == 0) {
            return false;
        }

        char[] c = s.toCharArray();
        if (!Character.isJavaIdentifierStart(c[0])) {
            return false;
        }

        for (int i = 1; i < c.length; i++) {
            if (!Character.isJavaIdentifierPart(c[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a new string with all the whitespace removed
     * 
     * @param s the source string
     * @return the string without whitespace or null
     */
    public static String removeWhiteSpace(String s) {
        String retn = null;

        if (s != null) {
            int len = s.length();
            StringBuffer sbuf = new StringBuffer(len);

            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);

                if (!Character.isWhitespace(c))
                    sbuf.append(c);
            }
            retn = sbuf.toString();
        }
        return retn;
    }

    /**
     * The default toString implementation of an object
     * 
     * @param object the object
     * @return a string in the form className@hexHashCode
     */
    public static final String defaultToString(Object object) {
        if (object == null)
            return "null";
        else
            return object.getClass().getName() + '@'
                   + Integer.toHexString(System.identityHashCode(object));
    }

    /**
     * The default toString implementation of an object creates string in the
     * form className@hexHashCode
     * 
     * @param object the object
     * @param buffer the string builder
     */
    public static final void defaultToString(JBossStringBuilder buffer, Object object) {
        if (object == null)
            buffer.append("null");
        else {
            buffer.append(object.getClass().getName());
            buffer.append('@');
            buffer.append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    /**
     * The default toString implementation of an object creates string in the
     * form className@hexHashCode
     * 
     * @param object the object
     * @param buffer the string buffer
     */
    public static final void defaultToString(StringBuffer buffer, Object object) {
        if (object == null)
            buffer.append("null");
        else {
            buffer.append(object.getClass().getName());
            buffer.append('@');
            buffer.append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    /**
     * Parses a time period into a long.
     *
     * Translates possible [msec|sec|min|h] suffixes
     *
     * For example:
     *   "1"      ->  1 (msec)
     *   "1msec   ->  1 (msec)
     *   "1sec"   ->  1000 (msecs)
     *   "1min"   ->  60000 (msecs)
     *   "1h"     ->  3600000 (msecs)
     * 
     * Accepts negative periods, e.g. "-1"
     * 
     * @param period the stringfied time period
     * @return the parsed time period as long
     * @throws NumberFormatException
     */
    public static long parseTimePeriod(String period) {
        try {
            String s = period.toLowerCase();
            long factor;

            // look for suffix
            if (s.endsWith("msec")) {
                s = s.substring(0, s.lastIndexOf("msec"));
                factor = MSEC;
            } else if (s.endsWith("sec")) {
                s = s.substring(0, s.lastIndexOf("sec"));
                factor = SECS;
            } else if (s.endsWith("min")) {
                s = s.substring(0, s.lastIndexOf("min"));
                factor = MINS;
            } else if (s.endsWith("h")) {
                s = s.substring(0, s.lastIndexOf("h"));
                factor = HOUR;
            } else {
                factor = 1;
            }
            return Long.parseLong(s) * factor;
        } catch (RuntimeException e) {
            // thrown in addition when period is 'null'
            throw new NumberFormatException("For input time period: '" + period + "'");
        }
    }

    /**
     * Same like parseTimePeriod(), but guards for negative entries.
     * 
     * @param period the stringfied time period
     * @return the parsed time period as long
     * @throws NumberFormatException
     */
    public static long parsePositiveTimePeriod(String period) {
        long retval = parseTimePeriod(period);
        if (retval < 0) {
            throw new NumberFormatException("Negative input time period: '" + period + "'");
        }
        return retval;
    }
}
