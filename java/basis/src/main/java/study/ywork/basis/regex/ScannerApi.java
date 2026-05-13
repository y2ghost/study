package study.ywork.basis.regex;

import java.util.Scanner;

class ScannerApi {
    public static void main(String[] args) {
        String str = "London:Rome#Paris::Munich///Moscow";
        Scanner scanner = new Scanner(str);
        scanner.useDelimiter("\\p{Punct}+");
        final String cityPattern = "\\p{L}+";

        while (scanner.hasNext()) {
            if (scanner.hasNext(cityPattern)) {
                System.out.println(scanner.next());
            } else {
                scanner.next();
            }
        }

        scanner.close();
    }
}
