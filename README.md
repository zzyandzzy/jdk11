# jdk11

# build
```bash
chmod +x ./configure

./configure --with-debug-level=slowdebug --enable-dtrace --with-jvm-variants=server --with-target-bits=64 --enable-ccache --with-num-cores=8 --with-memory-size=16384 --disable-warnings-as-errors --with-freetype=bundled --with-boot-jdk=bootstrap jdk
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
# build
make images

# clean
make clean && make dist-clean
```