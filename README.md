# jdk11

# build
```bash
# 安装freetype
brew install freetype

chmod +x ./configure

./configure --with-debug-level=slowdebug --enable-dtrace --with-jvm-variants=server --with-target-bits=64 --with-num-cores=8 --with-memory-size=16384 --disable-warnings-as-errors --with-freetype=bundled --with-boot-jdk=/Library/Java/JavaVirtualMachines/openjdk-11.jdk/Contents/Home
```

--with-debug-level=slowdebug 启用slowdebug级别调试

--enable-dtrace 启用dtrace

--with-jvm-variants=server 编译server类型JVM

--with-target-bits=64 指定JVM为64位

--enable-ccache 启用ccache，加快编译

--with-num-cores=8 编译使用CPU核心数

--with-memory-size=8000 编译使用内存

--disable-warnings-as-errors 忽略警告 , mac 使用 xcode 编译, 官方要求加上这个参数.

--with-freetype 设置freetype的路径

```bash
# 编译
make images
# 因为make版本太新导致的错误参考用
➜  ~ make --version 
GNU Make 4.2.1 
Built for x86_64-pc-linux-gnu 
Copyright (C) 1988-2016 Free Software Foundation, Inc. 
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html> 
This is free software: you are free to change and redistribute it. 
There is NO WARRANTY, to the extent permitted by law. 

gcc-9 (Homebrew GCC 9.3.0_2) 9.3.0
Copyright (C) 2019 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

# 因为gcc10太新导致的错误，建议用gcc 9
# https://bugs.openjdk.java.net/browse/JDK-8235903

# 清理
make clean && make dist-clean

# 快速编译jdk
make jdk
```

## 参考

- [写Java这么久，JDK源码编译过没？编译JDK源码踩坑纪实](https://segmentfault.com/a/1190000023251649)
