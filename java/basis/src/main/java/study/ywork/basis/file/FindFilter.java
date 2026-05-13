package study.ywork.basis.file;

import study.ywork.basis.file.enumeration.Conjunction;
import study.ywork.basis.file.enumeration.SizeMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FindFilter {
    SizeMode sizeMode;
    Conjunction conj;
    long size;
    String name;
    Pattern nameRE;
    boolean debug = false;

    void setSizeFilter(String sizeFilter) {
        System.out.println("FindFilter.setSizeFilter()");
        sizeMode = SizeMode.EQ;
        char c = sizeFilter.charAt(0);

        if (c == '+') {
            sizeMode = SizeMode.GT;
            sizeFilter = sizeFilter.substring(1);
        } else {
            if (c == '-') {
                sizeMode = SizeMode.LT;
                sizeFilter = sizeFilter.substring(1);
            }
        }

        size = Long.parseLong(sizeFilter);
    }

    public void addConjunction(Conjunction conj) {
        System.out.println("FindFilter.addConjunction()");
        if (this.conj != null) {
            throw new IllegalArgumentException("Only one conjucntion allowed in this version");
        }
        this.conj = conj;
    }

    void setNameFilter(String nameToFilter) {
        nameRE = makeNameFilter(nameToFilter);
    }

    Pattern makeNameFilter(String name) {
        StringBuilder sb = new StringBuilder("^");
        for (char c : name.toCharArray()) {
            switch (c) {
                case '.':
                    sb.append("\\.");
                    break;
                case '*':
                    sb.append(".*");
                    break;
                case '?':
                    sb.append('.');
                    break;
                case '[':
                    sb.append("\\[");
                    break;
                case ']':
                    sb.append("\\]");
                    break;
                case '(':
                    sb.append("\\(");
                    break;
                case ')':
                    sb.append("\\)");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        sb.append('$');
        if (debug) {
            System.out.println("RE=\"" + sb + "\".");
        }

        return Pattern.compile(sb.toString());
    }

    public boolean accept(Path p) throws IOException {
        if (debug) {
            System.out.println("FindFilter.accept(" + p + ")");
        }

        if (nameRE != null) {
            return nameRE.matcher(p.getFileName().toString()).matches();
        }

        if (sizeMode != null) {
            long sz = Files.size(p);
            switch (sizeMode) {
                case EQ:
                    return (sz == size);
                case GT:
                    return (sz > size);
                case LT:
                    return (sz < size);
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }
}
