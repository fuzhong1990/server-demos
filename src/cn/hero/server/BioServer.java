package cn.hero.server;

import cn.hero.handler.SocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Fuzhong on 2015/12/23.
 * ServerSocket是阻塞模式的,什么是阻塞,就是在没有任何连接之前,accept方法一直在那里阻塞着,直到有connection来继续往下执行.
 */

public class BioServer {

    public static void main (String [] args) throws IOException {
        int port = 9999;
        if (args != null && args.length > 0) {
            try{
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("The Bioserver server is start in port:" + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new SocketHandler(socket)).start();
            }
        }finally {
            if (server != null) {
                System.out.println("The Bioserver server close!");
                server.close();
                server = null;
            }
        }

    }

}
