package chat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class Sql2oUtil {
    private static Logger logger = LoggerFactory.getLogger(Sql2oUtil.class);
    private static Sql2o sql2o = null;
    private static Connection connection = null;

    public static Connection getConnection(){
        if (connection == null)
            init();
        return connection;
    }
    private static void init(){
        Properties properties = null;
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("db.properties");
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("error read db.properties");
        }
        String url = properties.getProperty("url");
        String user = properties.getProperty("username");
        String pass = properties.getProperty("password");
        sql2o = new Sql2o(url,user,pass);
        connection = sql2o.open();
    }
}
