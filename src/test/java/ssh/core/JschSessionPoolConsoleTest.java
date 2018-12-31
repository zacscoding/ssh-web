package ssh.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
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
        config.setMaxTotalPerKey(4);
        config.setMaxIdlePerKey(4);
        config.setMinIdlePerKey(3);
        config.setJmxEnabled(false);

        JSchSessionFactory factory = new JSchSessionFactory();
        JSchSessionPool pool = new JSchSessionPool(factory, config);

        final int threadCount = 1;
        final Thread[] threads = new Thread[threadCount];
        final Random random = new Random();

        IntStream.range(0, threadCount).forEach(i -> {
            threads[i] = new Thread(() -> {
                ServerDetails details = createDetails();
                Session session = null;
                try {
                    System.out.println("Try to create session : " + i);
                    session = pool.borrowObject(createDetails());
                    System.out.println("borrowSession : " + i + " :: " + session);
                    TimeUnit.SECONDS.sleep(random.nextInt(10) + 5);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("## will return :: " + i + ", session : " + session);
                    if (session != null) {
                        pool.returnObject(details, session);
                    }
                }
            });
            threads[i].setDaemon(true);
            threads[i].start();
        });

        TimeUnit.SECONDS.sleep(3L);
        ServerDetails details = createDetails();
        Session session = null;
        try {
            System.out.println("Try to create session : " + 1);
            session = pool.borrowObject(createDetails());
            System.out.println("borrowSession : " + 1 + " :: " + session);
            TimeUnit.SECONDS.sleep(random.nextInt(10) + 5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("## will return :: " + 1 + ", session : " + session);
            if (session != null) {
                pool.returnObject(details, session);
            }
        }

        for(Thread t : threads) {
            t.join();
        }
    }

    public ServerDetails createDetails() {
        ServerDetails details = ServerDetails.builder()
            .host("192.168.79.130")
            .port(22)
            .username("app")
            .userInfo(new UserInfoImpl("app", "apppw"))
            .build();

        return details;
    }
}