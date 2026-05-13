#include "lib2.h"
#include <iostream>

int main() {
  std::cout << "App2 main()" << std::endl;
  Lib2 lib2;
  std::cout << lib2.method() << std::endl;
  return 0;
}

