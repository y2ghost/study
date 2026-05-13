package study.ywork.servlet.registration;

public class PageOne implements Pagable {
    @Override
    public String getPageViewInfo() {
        return "视图一的信息";
    }

    @Override
    public String getPageModelInfo() {
        return "视图一的模型";
    }

    @Override
    public String getPath() {
        return "/pageOne";
    }
}
