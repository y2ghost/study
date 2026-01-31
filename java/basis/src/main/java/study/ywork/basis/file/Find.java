package study.ywork.basis.file;

import study.ywork.basis.file.enumeration.Conjunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Find {
    private static Logger logger = Logger.getLogger(Find.class.getSimpleName());
    private static boolean started;
    protected static FindFilter filter = new FindFilter();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            startWalkingAt(".");
            return;
        }

        int i = 0;
        while (i < args.length) {
            if (args[i].charAt(0) == '-') {
                switch (args[i].substring(1)) {
                    case "name":
                        Find.filter.setNameFilter(args[++i]);
                        break;
                    case "size":
                        Find.filter.setSizeFilter(args[++i]);
                        break;
                    case "a":
                        Find.filter.addConjunction(Conjunction.AND);
                        break;
                    case "o":
                        Find.filter.addConjunction(Conjunction.OR);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown argument " + args[i]);
                }
            }

            startWalkingAt(args[i]);
            i++;
        }

        if (!started) {
            startWalkingAt(".");
        }
    }

    public static void usage() {
        System.err.println(
                "Usage: Find [-n namefilter][-s sizefilter][dir...]");
        System.exit(1);
    }

    private static void startWalkingAt(String s) throws IOException {
        Path f = Path.of(s);
        String dirInfo = String.format("doName(%s)", s);
        logger.info(dirInfo);
        started = true;

        if (!Files.exists(f)) {
            System.out.println(s + " does not exist");
            return;
        }

        try (Stream<Path> files = Files.walk(f)) {
            files.forEach(fp -> {
                if (Files.isRegularFile(fp)) {
                    try {
                        doFile(fp);
                    } catch (IOException e) {
                        System.exit(1);
                    }
                } else if (Files.isDirectory(fp)) {
                    doDir(fp);
                } else {
                    System.err.println("Unknown type: " + s);
                }
            });
        }
    }

    private static void doFile(Path f) throws IOException {
        if (filter.accept(f)) {
            System.out.println("f " + f);
        }
    }

    private static void doDir(Path d) {
        System.out.println("d " + d.normalize());
    }
}
