# JVM

- [程序计数器](readme/JVMPC.md)
- [虚拟机栈](readme/JVMStack.md)
- [GC](readme/GC.md)

## 结构图

<img src="readme/static/image/JVM_Struct.png" alt="JVM结构图" style="zoom:50%;" />

## 线程模型

- 每个线程: 独立包括程序计数器、栈、本地方法栈
- 线程间共享: 堆、堆外内存(永久代或元空间、代码缓存)

<img src="readme/static/image/JVM_Thread_Struct.png" alt="JVM线程模型" style="zoom:50%;" />