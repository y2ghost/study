package study.ywork.basis.api.spi;

public class DefaultView implements MyView {
    private static final String VIEW_NAME = "DefaultView";

    @Override
    public String getName() {
        return VIEW_NAME;
    }
}
