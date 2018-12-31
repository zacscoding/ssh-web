package ssh.web.configuration;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ssh.core.pool.JSchSessionFactory;
import ssh.core.pool.JSchSessionPool;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class SshConfiguration {

    @Bean
    public JSchSessionFactory jSchSessionFactory() {
        return new JSchSessionFactory();
    }

    @Bean
    public JSchSessionPool jSchSessionPool() {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();

        config.setMaxTotal(10);
        config.setMaxTotalPerKey(5);
        config.setMaxIdlePerKey(5);
        config.setMinIdlePerKey(1);
        config.setJmxEnabled(false);

        return new JSchSessionPool(jSchSessionFactory(), config);
    }
}