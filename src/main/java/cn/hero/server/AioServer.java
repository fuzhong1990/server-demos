package cn.hero.server;

import cn.hero.handler.AioServerHandler;

/**
 * Created by Fuzhong on 2015/12/24.
 * AIO提供了两种异步操作的监听机制。
 * 第一种：通过返回一个Future对象来事件，调用其get（）会等到操作完成。
 * 第二种：类似于回调函数。在进行异步操作时，传递一个CompletionHandler，当异步操作结束时，会调用CompletionHandler.complete 接口
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
