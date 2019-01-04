package ssh.web.controller;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.annotation.PostConstruct;
import javax.print.DocFlavor.CHAR_ARRAY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import ssh.core.model.ServerDetails;
import ssh.core.model.ServerDetails.UserInfoImpl;
import ssh.web.service.SshService;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
public class SshController {

    private SshService sshService;

    @Autowired
    public SshController(SshService sshService) {
        this.sshService = sshService;
    }

    // TEMP
    /*@Autowired
    private SimpMessagingTemplate messagingTemplate;
    private ServerDetails serverDetails;
    private Session session;
    private Channel channel;

    @PostConstruct
    private void setUp() throws Exception {
        serverDetails = ServerDetails.builder()
            .host("192.168.79.130")
            .port(22)
            .username("app")
            .userInfo(UserInfoImpl.builder().user("app").password("apppw").build())
            .build();
        JSch jSch = new JSch();
        session = jSch.getSession(
            serverDetails.getUsername(), serverDetails.getHost(), serverDetails.getPort()
        );

        session.setPassword(serverDetails.getUserInfo().getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setUserInfo(serverDetails.getUserInfo());
        session.setTimeout(60000);

        session.connect();
        channel = session.openChannel("shell");
        channel.connect();
    }*/
    // -- TEMP

    /*@MessageMapping("/ssh/shell")
    public void execute(@Payload String message) throws IOException {
        log.info("## Receive message : " + message);
        PrintStream ps = new PrintStream(channel.getOutputStream());
        ps.println(message);
        ps.flush();
        readChannelOutput();
    }

    private void readChannelOutput() {
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
                    messagingTemplate.convertAndSend("/topic/ssh/shell", line);
                }

                if (line.contains("logout")) {
                    break;
                }

                if (line.endsWith("$")) {
                    break;
                }

                if (channel.isClosed()) {
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
    }*/
}
