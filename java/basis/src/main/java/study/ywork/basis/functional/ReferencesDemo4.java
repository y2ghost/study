package study.ywork.basis.functional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReferencesDemo4 {
    static final List<String> unsortedNames = List.of(
            "Gosling", "de Raadt", "Amdahl", "Turing", "Ritchie", "Hopper"
    );

    public static void main(String[] args) {
        List<String> names = new ArrayList<>(unsortedNames);
        Collections.sort(names, String::compareToIgnoreCase);

        names = new ArrayList<>(unsortedNames);
        Collections.sort(names, String::compareToIgnoreCase);
        dump(names);

        names = new ArrayList<>(unsortedNames);
        Collections.sort(names, (str1, str2) -> str1.compareToIgnoreCase(str2));
        dump(names);
        names = new ArrayList<>(unsortedNames);
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        dump(names);
    }

    private static void dump(List<String> names) {
        System.out.print(String.join(", ", names));
        System.out.println();
    }
}
