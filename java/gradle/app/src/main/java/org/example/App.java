package org.example;

import com.gradle.CustomLib;

public class App {
    public String getGreeting() {
        return "CustomLib identifier: " + CustomLib.identifier;
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}

