package study.ywork.servlet.registration;

public class PageTwo implements Pagable {
    @Override
    public String getPageViewInfo() {
        return "视图二的信息";
    }

    @Override
    public String getPageModelInfo() {
        return "视图二的模型";
    }

    @Override
    public String getPath() {
        return "/pageTwo";
    }
}