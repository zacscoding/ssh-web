package ssh.web.service;

import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssh.core.model.ServerDetails;
import ssh.core.pool.JSchSessionPool;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class SshService {

    private JSchSessionPool sessionPool;

    @Autowired
    public SshService(JSchSessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    public void connect(ServerDetails serverDetails) {
        try {
            Session session = sessionPool.borrowObject(serverDetails);
        } catch (Exception e) {

        }
    }

}
