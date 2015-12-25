package cn.hero.client;

import cn.hero.handler.AioClientHandler;

/**
 * Created by Fuzhong on 2015/12/24.
 */
public class AioClient {

    public static void main(String[] args) {
        int port = 9999;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new Thread(new AioClientHandler("127.0.0.1", port), "Aio-ClientHandler-001").start();
    }
}
