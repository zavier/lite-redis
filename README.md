# Redis RESP Simple Demo 
> REdis Serialization Protocol

Redis 服务器与客户端交互协议

相关文档：[https://github.com/antirez/redis-doc/blob/master/topics/protocol.md](https://github.com/antirez/redis-doc/blob/master/topics/protocol.md)

这是一个基本的 Redis RESP Demo，目前只实现了 string 类型的 SET GET 方法


使用方式：
1. 启动 RedisServer
2. 在 RedisClient中使用 Jedis 编码 redis 操作代码（目前只支持string类型的 set、get、del方法）


## Redis通信协议-RESP

Redis客户端与服务端通信使用 RESP(REdis Serialization Protocol)协议

它是一个序列化协议，支持如下几种数据类型，具体类型判断通过第一个字节判断，之间通过"\r\n"来分隔

- 简单字符串    以"+"开头
- 错误类型        以"-"开头
- 整数                以":"开头
- 块字符串        以"$"开头
- 数组                以"*"开头

**客户端每次发送一个块字符串数组到服务端，服务端根据命令执行后返回结果**

### 简单字符串

以"+"字符开头，后面接实际字符串，最后以"\r\n"结尾

因为字符是通过'\r\n'来判断结尾的，所以此种类型中的字符串内容就不能包含这特殊字符，如果有需要可以使用块字符串类型

例子：`+OK\r\n`

### 错误类型

以"-"字符开头，后面接着错误错误信息，最后以"\r\n"结尾

例子：`-Error message\r\n`

### 整数

以":"字符开头，数值，，最后以"\r\n"结尾

例子：`:1000\r\n`

### 块字符串

以"$"字符开头，后面是字符串的实际长度，之后以"\r\n"分隔，接着是字符串内容，最后以'\r\n'结尾

例子：

```
foobar : $6\r\nfoobar\r\n
// 为了方便阅读，可以简化为
$6
foobar
```

空字符串：`$0\r\n\r\n`

Null(不存在的值)：`$-1\r\n`

### 数组

以"*"开头，后面是数组长度，之后以"\r\n"分隔，后面是具体的其他的数据值(数据类型不要求一致)

空数组：`*0\r\n`

```
["1", "foo"]：*2\r\n$1\r\n1\r\n$3\r\nfoo\r\n
方便阅读，简化为：
*2           // 数组长度为2
$1           // 此元素为长度为1的简单字符
1            // 字符内容为"1"
$3           // 此元素为长度为3的简单字符
foo          // 字符内容为"foo"
```

如果是队列阻塞超时，则返回值为：`*-1\r\n`