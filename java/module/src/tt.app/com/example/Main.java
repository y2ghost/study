package com.example;

import ui.widget.WidgetController;
import util.common.StringUtil;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        WidgetController wc = new WidgetController();
        wc.display("a test string");
        List<String> list = StringUtil.split("a,b", ",");
        System.out.println(list);
    }
}

