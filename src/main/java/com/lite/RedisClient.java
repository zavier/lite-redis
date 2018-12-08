package com.lite;

import org.junit.Assert;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *  文档 <a href="https://redis.io/topics/protocol">https://redis.io/topics/protocol</a><br>
 *  + 简单字符串 <br>
 *  - 错误字符串 <br>
 *  : 整数字符串 <br>
 *  * 数组      值为数组长度 <br>
 *  $ 块字符串   值为字符串长度   如果数值大于0，则需要再读一行
 */
public class RedisClient {
    public static void main(String[] args) throws Exception {
        Jedis jedis = new Jedis("127.0.0.1", 6666);
        // 添加、查找
        jedis.set("key1", "value1");
        String res1 = jedis.get("key1");
        System.out.println("res1: " + res1);
        Assert.assertEquals("value1", res1);

        // 修改
        jedis.set("key1", "value2");
        String res2 = jedis.get("key1");
        System.out.println("res2: " + res2);
        Assert.assertEquals("value2", res2);

        // 删除
        jedis.del("key1");
        String res3 = jedis.get("key1");
        System.out.println("res3: " + res3);
        Assert.assertNull(res3);
    }
}
