package study.ywork.basis.api.spi;

public class ErrorView implements MyView {
    private static final String VIEW_NAME = "ErrorView";

    @Override
    public String getName() {
        return VIEW_NAME;
    }
}
