package com.example;

import easy.math.BasicCalc;

public class ModuleAnalysis {

  public static void main(String[] args) {
      analyze(ModuleAnalysis.class);
      System.out.println("------");
      analyze(BasicCalc.class);
  }

  private static void analyze(Class<?> cls) {
      System.out.println("class: " + cls);
      Module module = cls.getModule();
      System.out.println("Module: " + module);
      System.out.println("Name: " + module.getName());
      System.out.println("isNamed: " + module.isNamed());
      System.out.println("Descriptor: " + module.getDescriptor());
      System.out.println("isAutomatic: " + module.getDescriptor().isAutomatic());
  }
}

