# GC 垃圾回收器

## 标记阶段

### 引用计数算法

引用计数算法(Reference Counting)比较简单，对每个对象保存一个整型的引用计数器属性。用于记录对象被引用的情况。

对于一个对象A，只要有任何一个对象引用了A，则A的引用计数器就加1; 当引用失效时，引用计数器就减1。只要对象A的引用计数器的值为0，既表示对象A不可能再被使用，可进行回收。

优点: 实现简单、垃圾对象便于标识; **判定效率高**，回收没有延迟性

缺点:

- 需要单独的字段存储计数器，这样增加类**存储空间的开销**
- 每次赋值需要更新计数器，伴随着加法和减法的操作，这增加了**时间开销**
- 引用计数器有一个严重的bug，既无法处理**循环引用**的情况。所以导致了Java的垃圾回收器中没有使用到这类算法(Python使用了)。

### 可达性分析算法

GC Roots包括以下几种元素:

- 虚拟机栈中引用的对象。比如: 各个线程被调用的方法中使用到的参数、局部变量等
- 本地方法栈内JNI(通常说的本地方法)引用的对象。
- 方法区中类静态属性引用的对象。比如: 字符串常量池(String Table)里的引用
- 所有被同步锁synchroized持有的对象
- Java虚拟机内部的引用。比如: 基本数据类型对应的Class对象，一些常驻的异常对象(NullPointerException、OutOfMemoryError)，系统类加载器
- 反映Java虚拟机内部情况的JMXBean、JVMTI中注册的回调、本地嗲码缓存等。
- 除了这些固定的GC Roots集合以外，根据用户所选用的垃圾收集器以及当前回收的内存区域不同，还可以有其他对象"临时性"地加入，共同构成完整的GC Roots集合。比如: 分代收集和局部回收♻️(Partial GC)。
  - 如果只针对Java堆中的某一块区域进行垃圾回收(如: 只针对新生代)，必须考虑到内存区域锁虚拟机自己的实现细节，更不是孤立封闭的，这个区域的对象完全有可能被其他区域的对象所引用，这时候需要一并将关联的区域对象也加入GC Roots集合中去考虑，才能保证可达性分析的准确性。
- 小技巧: 由于GC Root采用栈方式存放变量和指针，所以如果一个指针，它保存了堆内存里面的对象，但是自己又不存放在堆内存里面，它就是一个Root。

注意:

- 如果要使用可达性分析算法来判断内存算法可以回收，那么分析工作必须在一个能保障一致性的快照中进行。这点不满足的话分析结果的准确性就无法保证。
- 这点也是导致GC进行时必须要**Stop The World**的一个重要原因。
  - 即使号称(几乎)不会发生停顿的CMS收集器中，枚举根节点时也是必须要停顿的。

## 对象的finalization机制

Java语言提供了对象终止(finalization)机制来允许开发人员提供对象被销毁之前的自定义处理逻辑。

当垃圾收集器发现没有引用指向一个对象，既: 垃圾回收此对象之前，总会先调用这个对象的`finaliz()`方法。

`finalize()`方法允许在子类中被重写，用于在对象被回收时进行资源释放。通常在这个方法中进行一些资源释放和清理工作，比如关闭文件、套接字和数据库连接🔗等。

永远不要主动的调用某个对象的`finalize()`方法，应该交给垃圾回收器机制调用。理由如下:

- 调用`finalize()`方法可能导致对象复活
- `finalize()`方法的执行时间时没有保障的，它完全由GC线程决定，极端情况下，若不发生GC，则`finalize()`方法将引用不会执行。
- 一个糟糕的`finalize()`会严重影响GC的性能。

如果从所有的根节点都无法访问某个对象，说明对象已经不再使用了，一般来说，此对象需要被回收。但事实上，也并非"非死不可"的，这时候它们暂时处于"缓刑"阶段。一个无法触及的对象有可能在某一个条件下"复活"自己，如果这样，那么它的回收就是不合理的，为此，定义虚拟机中的对象可能的3中状态。如下: 

- 可触及的: 从根节点开始，可以到达这个对象。
- 可复活的: 对象的所有引用都被释放，但对象有可能在`finalize()`中复活。
- 不可触及的: 对象的`finalize()`被调用，并且没有复活，那么就会进入不可触及状态。不可触及的对象不可能复活，因为`finalize()`只会被调用一次。

以上3中状态中，是由于`finalize()`方法的存在，进行的区分，只有在对象不可触及时才可以回收。

## 清除阶段

当成功区分出内存中存活对象和死亡对象后，GC 接下来的任务就是执行垃圾回收，释放掉无用对象所占用的内存空间，以便有足够的可用内存空间为新对象分配内存。

目前在JVM中比较常见的三种垃圾收集算法是标记一清除算法( Mark-Sweep)
复制算法（ Copying ）、标记 一 压缩算法( Mark-Compact )

### 标记-清除算法

标记-清除算法（ Mark-Sweep）是一种非常基础和常见的垃圾收集算法，该算法被J.McCarthy等人在1960年提出并并应用于Iisp语言。
执行过程：
当堆中的有效内存空间 (available memory）被耗尽的时候，就会停止整个程序（也被称为stop the world），然后进行两项工作，第一项则是标记，第二项则是清除。

标记：Collector 到用根节点开始遍历，标记所有被引用的对象。一般是在对象的Header中记录为可达对象。
清除：Co1lector对堆内存从头到尾进行线性的遍历，如果发现某个对象在其Header中没有标记为可达对象，则将其回收。

缺点：

- 效率不算高
- 在进行GC的时候，需要停止整个应用程序，导致用户体验差。
- 这种方式清理出来的空闲内存是不连续的，产生内存碎片。需要维护一个空闲列表。

### 复制算法



### 标记-压缩算法



## 分代收集算法



## 增量收集算法、分区算法
