# Redis RESP Simple Demo 
> REdis Serialization Protocol

Redis 服务器与客户端交互协议

相关文档：[https://redis.io/topics/protocol](https://redis.io/topics/protocol)

这是一个基本的 Redis RESP Demo，目前只实现了 string 类型的 SET GET 方法

代码写的很差劲，只是为了展示一下这个协议...

使用方式：
1. 启动 RedisServer
2. 在 RedisClient中使用 Jedis 编码 redis 操作代码（目前只支持string类型的 set、get、del方法）
