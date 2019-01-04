package ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayInputStream;
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
import org.apache.logging.log4j.simple.SimpleLogger;
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
            //.host("192.168.79.130")
            .host("192.168.5.78")
            .port(22)
            .username("app")
            .userInfo(UserInfoImpl.builder().user("app").password("apppw").build())
            .build();
    }

    @Test
    public void temp() throws Exception {
        System.out.println(ConsoleColors.RED + "RED COLORED" +
            ConsoleColors.RESET + " NORMAL");
    }

    public static class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    }

    public static void main(String[] args) throws Exception {
        Session session = null;
        try {
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

            System.out.println(">> Connect :: " + session.isConnected());

            // WTD :: send command 2 & print to uppercase
            ChannelShell channel = (ChannelShell) session.openChannel("shell");

            // stream

            // OUTPUT
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // channel.setOutputStream(baos);

            // startReadThread2(baos);
            // channel.setOutputStream(System.out);
            channel.setOutputStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    System.out.print(Character.toUpperCase((char) b));
                }
            });

            // INPUT
            PipedOutputStream pipe = new PipedOutputStream();
            PipedInputStream in = new PipedInputStream(pipe);
            PrintWriter pw = new PrintWriter(pipe);
            channel.setInputStream(in, true);

            pw.print("ll\r");
            pw.flush();

            channel.connect();

            System.out.println(">> Success to connect");
            TimeUnit.SECONDS.sleep(3L);

            pw.print("pwd\r");
            pw.flush();

            TimeUnit.SECONDS.sleep(5L);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private static void startReadThread2(final ByteArrayOutputStream baos) {
        Thread t = new Thread(() -> {
            int size = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println(baos.toString());

                    /*int newSize = baos.size();
                    System.out.printf("## Check size : %d | %d", size, newSize);

                    if (size != newSize) {
                        System.out.println("Read >> " + baos.toString());
                        baos.reset();
                    }

                    size = baos.size();*/
                    TimeUnit.MINUTES.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

            }
        });
        t.setDaemon(true);
        t.start();
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
