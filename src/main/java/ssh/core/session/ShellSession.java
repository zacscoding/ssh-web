package ssh.core.session;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2019-01-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class ShellSession {

    private Session session;
    private boolean running;
    private ChannelShell channel;
    private Thread readTask;

    // write
    private PipedOutputStream poutWrapper;
    private PipedInputStream pin;

    // read
    private PipedInputStream pinWrapper;
    private PipedOutputStream pout;

    public ShellSession(Session session) {
        Objects.requireNonNull(session, "session must be not null");
        this.session = session;
        setupStream();
    }

    /**
     * Start Shell Channel
     */
    public void start(Consumer<String> onResponse) throws JSchException {
        Objects.requireNonNull(onResponse, "onResponse must be not null");

        if (!session.isConnected()) {
            throw new RuntimeException("Session must be connected");
        }

        channel = (ChannelShell) session.openChannel("shell");
        channel.setInputStream(pin);
        channel.setOutputStream(pout);

        channel.connect();

        this.running = true;
        startReadThread(onResponse);
    }

    /**
     * Write message
     */
    public void write(String message) throws IOException {
        if (!StringUtils.hasText(message)) {
            return;
        }

        poutWrapper.write(message.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Setup streams
     */
    private void setupStream() {
        try {
            // write
            this.pin = new PipedInputStream(4096);
            this.poutWrapper = new PipedOutputStream(pin);

            // read
            this.pinWrapper = new PipedInputStream(4096);
            this.pout = new PipedOutputStream(pinWrapper);
        } catch (IOException e) {
            log.error("IOException occur while create stream", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * start read response task
     */
    private void startReadThread(final Consumer<String> onResponse) {
        readTask = new Thread(() -> {
            while (running) {
                try {
                    if (onResponse != null && pinWrapper.available() != 0) {
                        StringBuilder sb = new StringBuilder();

                        while (pinWrapper.available() > 0) {
                            sb.append((char) pinWrapper.read());
                        }

                        onResponse.accept(sb.toString());
                    }

                    Thread.sleep(100L);
                } catch (IOException e) {
                    log.warn("IOException occur while read input stream", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        });

        readTask.setDaemon(true);
        readTask.start();
    }
}
