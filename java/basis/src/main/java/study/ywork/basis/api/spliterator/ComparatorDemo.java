package study.ywork.basis.api.spliterator;

import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;

public class ComparatorDemo {
    public static void main(String[] args) {
        SortedSet<Test> set = new TreeSet<>((o1, o2) -> o1.str.compareTo(o2.str));
        set.add(new Test("two"));
        set.add(new Test("one"));
        Spliterator<Test> s = set.spliterator();
        System.out.println(s.getComparator());
        System.out.println(set);
    }

    private static class Test {
        private final String str;

        private Test(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return "Test [str=" + str + "]";
        }
    }
}
