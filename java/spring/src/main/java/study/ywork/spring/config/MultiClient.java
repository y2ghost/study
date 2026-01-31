package study.ywork.spring.config;

public class MultiClient {
    private MyDataSource myDataSource;

    // 构造函数自动注入依赖
    public MultiClient(MyDataSource myDataSource) {
        this.myDataSource = myDataSource;
    }

    public void showData() {
        System.out.println(myDataSource.getData());
    }
}
