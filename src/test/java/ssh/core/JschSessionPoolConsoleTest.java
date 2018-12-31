package ssh.core;

import com.jcraft.jsch.Session;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.junit.Test;
import ssh.core.model.ServerDetails;
import ssh.core.model.ServerDetails.UserInfoImpl;
import ssh.core.pool.JSchSessionFactory;
import ssh.core.pool.JSchSessionPool;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
public class JschSessionPoolConsoleTest {

    @Test
    public void consoleTest() throws Exception {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMaxTotalPerKey(2);
        config.setMaxIdlePerKey(1);
        config.setMinIdlePerKey(1);
        config.setJmxEnabled(false);

        JSchSessionFactory factory = new JSchSessionFactory();
        JSchSessionPool pool = new JSchSessionPool(factory, config);

        ServerDetails details = ServerDetails.builder()
            .host("192.168.5.78")
            .port(22)
            .username("app")
            .userInfo(new UserInfoImpl("app", "apppw"))
            .build();

        Session firstSession = pool.borrowObject(details);
        System.out.println(">> Create session : " + firstSession);

        Session secondSession = pool.borrowObject(details);
        System.out.println(">> Create session : " + secondSession);

        System.out.println("## >> Sleep..");
        TimeUnit.SECONDS.sleep(5L);
        pool.returnObject(details, firstSession);


        Session thirdSession = pool.borrowObject(details);
        System.out.println(">> Create session : " + thirdSession);

        System.out.println(">> is connected : " + firstSession.isConnected());
        System.out.println(">> is connected : " + secondSession.isConnected());

        if(firstSession.isConnected()) {
            firstSession.disconnect();
        }

        if(secondSession.isConnected()) {
            secondSession.disconnect();
        }

        if(thirdSession.isConnected()) {
            thirdSession.disconnect();
        }
    }
}
