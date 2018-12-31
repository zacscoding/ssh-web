package ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import ssh.core.model.ServerDetails;
import ssh.core.model.ServerDetails.UserInfoImpl;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    ServerDetails details;

    @Before
    public void setUp() {
        details = ServerDetails.builder()
            .host("192.168.79.130")
            .port(22)
            .username("app")
            .userInfo(UserInfoImpl.builder().user("app").password("apppw").build())
            .build();
    }

    @Test
    public void temp() throws Exception {
        System.out.println(System.in.getClass());
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Session session = null;
        try {
            ServerDetails details = ServerDetails.builder()
                .host("192.168.79.130")
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

            session.connect();

            System.out.println(">> Connect :: " + session.isConnected());

            // WTD :: send command 2 & print to uppercase
            ChannelShell channel = (ChannelShell) session.openChannel("shell");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channel.setOutputStream(baos);

            PipedOutputStream pipe = new PipedOutputStream();
            PipedInputStream in = new PipedInputStream(pipe);
            PrintWriter pw = new PrintWriter(pipe);

            System.out.println(baos.toString());

            pw.println("ll");
            pw.flush();

            channel.setInputStream(in, true);

            channel.connect();
            channel.start();

            TimeUnit.SECONDS.sleep(5L);

            System.out.println(baos.toString());
            TimeUnit.SECONDS.sleep(30L);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private static void startReadThread(Channel channel) {
        Thread readThread = new Thread(() -> {
            byte[] readByte = new byte[1024];
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    InputStream is = channel.getInputStream();

                    while (is.available() > 0) {
                        int len = is.read(readByte);

                        if (len < 0) {
                            break;
                        }

                        System.out.print(new String(readByte, 0, len));
                    }

                    TimeUnit.MILLISECONDS.sleep(20L);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        readThread.setDaemon(true);
        readThread.start();
    }

    private static void readChannelOutput(Channel channel) {
        byte[] buffer = new byte[1024];
        try {
            InputStream in = channel.getInputStream();
            String line = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    //messagingTemplate.convertAndSend("/topic/ssh/shell", line);
                    System.out.println(line);
                }

                if (line.contains("logout")) {
                    break;
                }

                if (channel.isClosed()) {
                    break;
                }

                if (line.endsWith("$") || line.endsWith("#")) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
        } catch (Exception e) {
            System.out.println("Error while reading channel output: " + e);
        }
    }
}
