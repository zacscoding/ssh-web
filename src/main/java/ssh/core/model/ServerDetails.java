package ssh.core.model;

import com.jcraft.jsch.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2018-12-31
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"host", "username", "port"})
public class ServerDetails {

    private String host;
    private String username;
    private int port;
    private UserInfoImpl userInfo;


    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class UserInfoImpl implements UserInfo {

        private String user;
        private String password;

        @Builder
        public UserInfoImpl(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        public String getPassphrase() {
            return null;
            // return password;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            // System.out.println("promptPassword :: " + message);
            // return true;
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            // System.out.println("promptPassphrase :: " + message);
            // return true;
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            // System.out.println("promptYesNo :: " + message);
            // return true;
            return false;
        }

        @Override
        public void showMessage(String message) {
            // System.out.println("showMessage :: " + message);
        }
    }
}
