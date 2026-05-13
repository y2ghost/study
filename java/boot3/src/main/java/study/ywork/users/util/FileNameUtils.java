package study.ywork.users.util;

public class FileNameUtils {
    public static String getExtension(final String filename) {
        if (filename == null) {
            return null;
        }

        final int extensionPosition = filename.lastIndexOf('.');
        if (extensionPosition < 0) {
            return "";
        }
        return filename.substring(extensionPosition + 1);
    }

    public static String getBaseName(final String filename) {
        if (filename == null) {
            return null;
        }

        final int extensionPosition = filename.lastIndexOf('.');
        if (extensionPosition < 0) {
            return filename;
        }

        return filename.substring(0, extensionPosition);
    }

    private FileNameUtils() {
        // NOOP
    }
}
