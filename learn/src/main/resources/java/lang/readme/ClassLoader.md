# ClassLoader

安装插件 `jclasslib Bytecode Viewer`

## 初始化

- 初始化阶段就是执行类的构造器方法<clinit>()的过程

- 此方法不需要定义，是javac编译器自动收集类中的所有变量的赋值动作和静态代码块中的语句合并而来，参考[ClassInitTest](../../../../../test/java/cool/intent/java/lang/classloader/ClassInitTest.java)

- 构造器方法中的指令都按语句在源文件中出现的顺序执行。如[ClassInitTest_ASM.txt](../../../../../test/java/cool/intent/java/lang/classloader/ClassInitTest_ASM.txt)

- <clinit>()不同于类的构造器。(构造器是虚拟机视角下的<init>())

- 若该类具有父类，JVM会保证子类的<clinit>()执行之前，父类的<clinit>()已经执行完毕，参考[ClassInitExtendsTest](../../../../../test/java/cool/intent/java/lang/classloader/ClassInitExtendsTest.java)

- 虚拟机必须保证一个类的<clinit>()方法在多线程被同步加锁，参考[ClassInitThreadTest](../../../../../test/java/cool/intent/java/lang/classloader/ClassInitThreadTest.java)