package com.lite;

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
        Socket socket = new Socket("127.0.0.1", 6379);
        try (PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            // INFO ALL
//            out.write("*1\r\n$7\r\nINFO\r\n$3\r\nALL\r\n");
//            out.write("*6\r\n$4\r\nscan\r\n$1\r\n0\r\n$5\r\nMATCH\r\n$1\r\n*\r\n$5\r\nCOUNT\r\n$5\r\n10000\r\n");
//            out.write("*2\r\n$6\r\nselect\r\n$1\r\n0\r\n");
            out.write("*2\r\n$3\r\nDEL\r\n$7\r\nclj-key\r\n");
            out.flush();
            String s = null;
            while ((s = in.readLine()) != null) {
                System.out.println(s + "==");
            }
        }
    }
}
