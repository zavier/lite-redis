package com.lite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  文档 <a href="https://redis.io/topics/protocol">https://redis.io/topics/protocol</a><br>
 *  + 简单字符串 <br>
 *  - 错误字符串 <br>
 *  : 整数字符串 <br>
 *  * 数组      值为数组长度 <br>
 *  $ 块字符串   值为字符串长度   如果数值大于0，则需要再读一行
 */
public class RedisServer {
    private static Map<String, String> dataMap = new HashMap<>();

    private static List<String> commands = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6666);
        Socket socket = serverSocket.accept();
        try (PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            int commandLen = 0;
            String content = null;
            while ((content = in.readLine()) != "END") {
                System.out.println(content);
                // 初始化一个客户端命令的长度
                if (content.startsWith("*") && commandLen == 0) {
                    commandLen = Integer.valueOf(content.substring(1, 2));
                    continue;
                }

                // 获取数组中客户端的一个命令
                if (commandLen > 0) {
                    commandLen--;
                }
                if (content.startsWith("$")) {
                    // 获取块字符串需要读取两行，此处回补一下
                    commandLen++;
                    continue;
                }
                commands.add(content);

                if (commandLen == 0) {
                    executeCommand(out);
                }
            }
        }
    }

    private static void executeCommand(PrintWriter out) {
        System.out.println("COMMANDS: " + commands);
        int i = 0;
        String command = commands.get(i);
        switch (command.toUpperCase()) {
            case "GET":
                String key = commands.get(i+1);
                String value = dataMap.get(key);
                returnSimpleResult(out, "$" + value.length() + " " + value + " ");
                break;
            case "SET":
                dataMap.put(commands.get(i+1), commands.get(i+2));
                returnSimpleResult(out, "+OK ");
                break;
            case "DEL":
                dataMap.remove(commands.get(i+1));
                returnSimpleResult(out, ":1 ");
                break;




            case "SCAN":
                int size = dataMap.size();
                if (size == 0) {
                    returnSimpleResult(out, "*2 $1 0 *0 ");
                    return;
                }

                Set<String> keys = dataMap.keySet();
                // 第一个参数是告诉scan是否已经查完，到结尾了
                StringBuilder sb = new StringBuilder("*2 $1 0 *" + keys.size() + " ");
                for (String k : keys) {
                    sb.append("$").append(k.length()).append(" ")
                            .append(k).append(" ");
                }
                returnSimpleResult(out, sb.toString());
                break;
            case "PING":
            returnSimpleResult(out, "+PONG ");
            break;
            case "INFO":
            // 工具会查看版本，判断是否能使用一些命令，如scan
            returnOriResult(out, "$30\r\n# Server\nredis_version:3.2.100\r\n");
            break;
            case "SELECT":
            // 假装只有 DB0
            if (commands.get(i+1).equals("0")) {
                    returnSimpleResult(out, "+OK ");
                    return;
                } else {
                    returnOriResult(out, "-ERR invalid DB index\r\n");
                }
            break;
            case "TYPE":
                returnSimpleResult(out, "+string ");
                break;
            case "TTL":
                returnSimpleResult(out, ":-1 ");
                break;
        }
    }

    private static void returnSimpleResult(PrintWriter out, String res) {
        String result = res.replace(" ", "\r\n");
        returnOriResult(out, result);
    }

    private static void returnOriResult(PrintWriter out, String result) {
        out.write(result);
        out.flush();
        commands.clear();
        System.out.println("RETURN [\n" + result + "]");
    }

}
