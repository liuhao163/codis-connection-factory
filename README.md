# codis-connection-factory

在Spring项目中基于支持用RedisTemplate，注入CodisConnectionFactory适配。

## Configure示例:
```java
@Configuration
public class CodisConfigure {

    @Value("${spring.codis.zookeeper.url}")
    private String zookeeperUrl;
    @Value("${spring.codis.zkproxy.dir}")
    private String zkproxyDir;


    private CoidsConnectionFactory coidsConnectionFactory;

    @Bean
    public CoidsConnectionFactory coidsConnectionFactory() {
        coidsConnectionFactory = new CoidsConnectionFactory();
        coidsConnectionFactory.setZkProxyDir(zkproxyDir);
        coidsConnectionFactory.setZkServer(zookeeperUrl);
        return coidsConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> getRedisTemplate() {
        return new StringRedisTemplate(coidsConnectionFactory);
    }

}

```
## 配置文件如下
```$xslt
spring.codis.zookeeper.url=localhost:2181
spring.codis.zkproxy.dir=/jodis/icorp
```

