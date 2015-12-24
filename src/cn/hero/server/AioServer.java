package cn.hero.server;

import cn.hero.handler.AioServerHandler;

/**
 * Created by Fuzhong on 2015/12/24.
 */
public class AioServer {

    public static void main(String[] args) {
        int port = 9999;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
         }
        AioServerHandler serverHandler = new AioServerHandler(port);
        new Thread(serverHandler, "Aio-ServerHandler-001").start();
    }
}
