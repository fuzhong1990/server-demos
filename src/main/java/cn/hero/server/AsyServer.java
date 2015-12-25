package cn.hero.server;

import cn.hero.handler.ServerHandlerExecutePool;
import cn.hero.handler.SocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Fuzhong on 2015/12/24.
 */
public class AsyServer {

    public static void main (String [] args) {
        int port = 9999;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The AsyServer server is start in port : " + port);
            Socket socket = null;
            ServerHandlerExecutePool singleExecutor = new ServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {

        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
