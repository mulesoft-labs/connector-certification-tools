package org.mule.tools.devkit.sonar;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Immutable public class ValidationErrorImpl implements ValidationError {

    private final Rule.Documentation doc;
    private final String message;
    private final boolean isUnique;

    ValidationErrorImpl(Rule.@NonNull Documentation doc, @NonNull String message) {
        this(doc, message, true);
    }

    public ValidationErrorImpl(Rule.@NonNull Documentation doc, @NonNull String message, boolean isUnique) {
        this.doc = doc;
        this.message = message;
        this.isUnique = isUnique;

    }

    @Override public Rule.@NonNull Documentation getDocumentation() {
        return doc;
    }

    @Override public @NonNull String getMessage() {
        return message;
    }

    @Override public @NonNull String getUUID() {
        return doc.getId() + (!isUnique ? ":" + computeMD5(message) : "");
    }

    private static String computeMD5(String string) {
        String result;
        try {
            byte[] stringBytes = string.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(stringBytes, 0, stringBytes.length);
            result = new BigInteger(1, messageDigest.digest()).toString(16);
            result = StringUtils.leftPad(result, 32, '0');
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
