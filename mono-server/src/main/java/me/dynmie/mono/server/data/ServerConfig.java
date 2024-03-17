package me.dynmie.mono.server.data;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author dynmie
 */
@Getter
public class ServerConfig {

    private final NetworkInformation networkInformation = new NetworkInformation();
    private final LoginCredentials loginCredentials = new LoginCredentials();

    @Getter
    @Setter
    public static class NetworkInformation {
        private String host = "localhost";
        private int port = 3991;

        public InetAddress getAddress() {
            try {
                return InetAddress.getByName(host);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Getter
    @Setter
    public static class LoginCredentials {
        private String token = "poggers";
    }

}
