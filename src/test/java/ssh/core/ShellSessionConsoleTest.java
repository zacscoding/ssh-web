package ssh.core;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.concurrent.TimeUnit;
import ssh.core.model.ServerDetails;
import ssh.core.model.ServerDetails.UserInfoImpl;
import ssh.core.session.ShellSession;

/**
 * @author zacconding
 * @Date 2019-01-04
 * @GitHub : https://github.com/zacscoding
 */
public class ShellSessionConsoleTest {

    public static void main(String[] args) throws Exception {
        Session session = null;
        ServerDetails details = ServerDetails.builder()
            .host("192.168.5.78")
            .port(22)
            .username("app")
            .userInfo(UserInfoImpl.builder().user("app").password("apppw").build())
            .build();

        JSch jSch = new JSch();
        session = jSch.getSession(details.getUsername(), details.getHost(), details.getPort());

        session.setPassword(details.getUserInfo().getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setUserInfo(details.getUserInfo());
        session.setTimeout(60000);
        session.setDaemonThread(true);

        session.connect();

        ShellSession shellSession = new ShellSession(session);
        shellSession.start(res -> System.out.print(res.toUpperCase()));

        shellSession.write("ll\n");
        TimeUnit.SECONDS.sleep(2L);
        shellSession.write("pwd\n");
        TimeUnit.SECONDS.sleep(5L);
    }
}
