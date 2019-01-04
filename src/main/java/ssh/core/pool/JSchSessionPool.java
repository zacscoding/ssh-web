package ssh.core.pool;

import com.jcraft.jsch.Session;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import ssh.core.model.ServerDetails;

/**
 * JSch Session pool
 *
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
public class JSchSessionPool extends GenericKeyedObjectPool<ServerDetails, Session> {

    public JSchSessionPool(KeyedPooledObjectFactory<ServerDetails, Session> factory,
        GenericKeyedObjectPoolConfig<Session> config) {

        super(factory, config);
    }
}
