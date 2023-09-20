脚本概述
- clean.sh: 清理
- noLib.sh: 直接编译构建
- sharedLib.sh: 动态库方式构建
- staticLib.sh: 静态库方式构建

工具说明
- ldd用于查看依赖的动态库信息
- objdump查看库文件信息
- readelf查看ELF相关信息
- nm查看库里面的符号信息
  > 示例: nm -A /usr/lib64/lib*.so 2> /dev/null | grep 'crypt$'
- ldocnfig用于搜索动态库路径
  > 搜索缓存文件/etc/ld.so.cache
  > ldconfig -p打印动态库版本信息

动态库名称概述
- 实际动态库名称: libname.so.maj.min
  > 实际的库文件
- 动态库SO名称: libname.so.maj
  > 库文件的软连接，程序链接或运行时使用的名称
- 动态库链接名称: libname.so
  > 库文件的软连接，可用于无版本的动态库链接

动态库搜索路径概述
- 程序自身rpath路径(库里面维护的DT_RPATH信息)
- LB_LIBRARY_PATH环境变量指定的路径
- 程序自身runpath路径(库里面维护的DT_RUNPATH信息)
- /etc/ld.so.cache里面查找路径
- 系统的库路径(/lib64,/usr/lib64...)

指定动态库运行时搜索路径示例
- gcc -g -Wall -Wl,-rpath,/home/mtk/pdir -o prog prog.c libdemo.so
- LD_RUN_PATH变量和参数(-rpath)类似

测试动态库示例
- LD_PRELOAD=libalt.so ./prog
- LD_DEBUG=help date
