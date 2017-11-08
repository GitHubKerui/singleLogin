import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;


/**
 * http://www.cnblogs.com/liuling/p/2014-4-19-04.html
 * @author eblly
 * @since 2017/11/8
 */
public class JedisTs {

    private Jedis jedis;

    @Before
    public void setup() {
        //连接redis服务器，127.0.0.1:6379
        jedis = new Jedis("127.0.0.1", 6379);
        //权限认证
//        jedis.auth("admin");
    }

    /**
     * redis存储字符串
     */
    @Test
    public void testString() {
        //-----添加数据----------
        jedis.set("name","xinxin");//向key-->name中放入了value-->xinxin
        System.out.println(jedis.get("name"));//执行结果：xinxin

        jedis.append("name", " is my lover"); //拼接
        System.out.println(jedis.get("name"));

        jedis.del("name");  //删除某个键
        System.out.println(jedis.get("name"));

        //设置多个键值对
        jedis.mset("name","liuling","age","23","qq","476777XXX");
        jedis.incr("age"); //进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }


    /**
     * jedis操作List
     */
    @Test
    public void testList(){
        //开始前，先移除所有的内容
        jedis.del("java");
        System.out.println(jedis.lrange("java",0,-1));
        //先向key java中存放三条数据
        jedis.lpush("java","spring");
        jedis.lpush("java","struts");
        jedis.lpush("java","hibernate");
        //再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java",0,-1));

        jedis.del("java");
        jedis.rpush("java","spring");
        jedis.rpush("java","struts");
        jedis.rpush("java","hibernate");
        System.out.println(jedis.lrange("java",0,-1));
    }
}
