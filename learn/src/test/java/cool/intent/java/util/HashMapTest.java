package cool.intent.java.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/7/15 9:15 下午
 * @since 1.0
 */
class HashMapTest {
    Map<Node, Integer> map;

    @BeforeEach
    void before() {
        map = new HashMap<>(2);
        for (int i = 0; i < 100; i++) {
            Node node = new Node(i);
            map.put(node, i);
        }
    }

    @Test
    void test(){
    }

    @Test
    void testMulThreadPutConcurrent() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            map.get(new Node(2));
        });
        Thread thread2 = new Thread(() -> {
            map.get(new Node(2));
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

}
