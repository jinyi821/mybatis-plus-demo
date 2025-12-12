package com.example.mybatisplusdemo;

import java.util.ArrayList;
import java.util.List;

public class G1Demo {
    // 故意弄一个超大对象（> 50% region 时会直接进 Humongous Region）
    static class BigObject {
        byte[] data = new byte[2 * 1024 * 1024]; // 2MB
    }

    // 普通小对象
    static class User {
        long userId;
        String name = "user-" + System.nanoTime();
        BigObject cache; // 持有大对象引用，防止被回收
        User next;
    }

    public static void main(String[] args) throws InterruptedException {
        // 强制使用 G1 + 小堆 + 固定 region 大小，方便观察
        // JVM 参数：
        // -Xms64m -Xmx64m -XX:+UseG1GC -XX:G1HeapRegionSize=2m 
        // -XX:MaxGCPauseMillis=200 -Xlog:gc*,safepoint*:file=gc.log:time,uptime,level,tags

        System.out.println("G1 垃圾回收演示开始...");

        // 第1阶段：疯狂创建短生命周期对象 → 触发 Young GC
        for (int i = 0; i < 20_000; i++) {
            User user = new User();           // Eden 区疯狂分配
            user.next = new User();           // 形成链表
            if (i % 1000 == 0) {
                Thread.sleep(1);                  // 模拟业务处理
            }
        }

        // 第2阶段：制造老年代垃圾
        List<User> oldGarbage = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User u = new User();
            u.cache = new BigObject();        // 2MB 大对象
            oldGarbage.add(u);                // 强引用，暂时不死
        }

        System.out.println("老年代垃圾制造完成， 准备丢弃引用...");
        Thread.sleep(2000);

        // 第3阶段：丢弃强引用 → 老年代变成纯垃圾
        oldGarbage = null;                    // 一瞬间 5000 个 2MB 对象全变垃圾 ≈ 10GB 垃圾！
        System.gc();                          // 建议 GC（G1 通常会忽略，但演示用）

        // 第4阶段：再疯狂创建新对象，逼 G1 必须回收老年代
        System.out.println("开始制造晋升压力，逼出 Mixed GC...");
        List<User> survivors = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            User u = new User();
            if (i % 7 == 0) survivors.add(u); // 让少量对象存活，模拟真实业务
            if (i % 5000 == 0) {
                Thread.sleep(10);
                System.out.println("当前存活对象数: " + survivors.size());
            }
        }

        Thread.sleep(12000); // 让 G1 有时间完成并发标记和 Mixed GC
    }
}