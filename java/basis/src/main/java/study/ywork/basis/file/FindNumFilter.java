package study.ywork.basis.file;

class FindNumFilter {
    int num;
    static final int LE = -2;
    static final int LT = -1;
    static final int EQ = 0;
    static final int GT = 1;
    static final int GE = 2;
    int mode = EQ;

    FindNumFilter(String input) {
        switch (input.charAt(0)) {
            case '+':
                mode = GT;
                break;
            case '-':
                mode = LT;
                break;
            case '=':
                mode = EQ;
                break;
            default:
                // 不做事儿
        }
        num = Math.abs(Integer.parseInt(input));
    }

    FindNumFilter(int mode, int value) {
        this.mode = mode;
        num = value;
    }

    boolean accept(int n) {
        switch (mode) {
            case GT:
                return n > num;
            case EQ:
                return n == num;
            case LT:
                return n < num;
            default:
                System.err.println("UNEX CASE " + mode);
                return false;
        }
    }
}
