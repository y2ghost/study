编译
- 下载源代码(https://github.com/Mbed-TLS/)
- 解压源代码
- 编译: make
- 测试: make check
- 交叉编译: make CC=arm-linux-gnueabi-gcc AR=arm-linux-gnueabi-ar

PSA-Crypto-API说明
- 需要使用psa_crypto_init()进行初始化
- 导入KEY使用psa_import_key进行导入

