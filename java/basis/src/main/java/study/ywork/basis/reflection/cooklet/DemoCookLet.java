package study.ywork.basis.reflection.cooklet;

public class DemoCookLet extends CookLet {
    public void work() {
        System.out.println("I am busy baking cookies.");
    }

    public void terminate() {
        System.out.println("I am shutting down my ovens now.");
    }
}
