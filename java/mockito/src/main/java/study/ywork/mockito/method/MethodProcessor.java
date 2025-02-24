package study.ywork.mockito.method;

public class MethodProcessor {
    private MethodService service;

    public MethodProcessor(MethodService service) {
        this.service = service;
    }

    public String process() {
        int returnInteger = service.doSomething();
        return formatInteger(returnInteger);
    }

    public String process2() throws Exception {
        int returnInteger = service.doSomething2();
        return formatInteger(returnInteger);
    }

    public String process3() {
        try {
            int returnInteger = service.doSomething2();
            return formatInteger(returnInteger);
        } catch (Exception e) {
            return "default-value";
        }
    }

    private String formatInteger(int returnInteger) {
        return String.format("My Integer is: %d", returnInteger);
    }
}