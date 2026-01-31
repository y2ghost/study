package study.ywork.basis.swing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.UIManager;

public class UiDefaultsList {
    public static void main(String[] args) {
        List<Map.Entry<Object, Object>> entries = new ArrayList<>(UIManager.getLookAndFeelDefaults().entrySet());
        entries.sort(Comparator.comparing(e -> Objects.toString(e.getKey())));
        entries.forEach(UiDefaultsList::printEntry);
    }

    private static void printEntry(Map.Entry<Object, Object> e) {
        System.out.printf("%-53s= %s%n", e.getKey(), e.getValue().getClass().getTypeName());
    }
}
