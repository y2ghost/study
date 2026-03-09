package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class PosixFilePermissionsViewDemo {
    private static final String TEST_FILE = "/tmp/id";

    public static void main(String[] args) throws IOException {
        Path filePath = Path.of(TEST_FILE);
        Files.deleteIfExists(filePath);
        Set<PosixFilePermission> nPerms = Set.of(PosixFilePermission.OWNER_READ,
                PosixFilePermission.GROUP_READ);
        Files.createFile(filePath,
                PosixFilePermissions.asFileAttribute(nPerms));
        PosixFileAttributes attrs =
                Files.getFileAttributeView(filePath,
                                PosixFileAttributeView.class)
                        .readAttributes();
        System.out.format("File %s Owned by %s has perms %s%n",
                filePath,
                attrs.owner().getName(),
                PosixFilePermissions.toString(attrs.permissions()));
    }
}
