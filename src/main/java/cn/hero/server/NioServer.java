package cn.hero.server;

import cn.hero.handler.MultiplexerServer;

/**
 * Created by Fuzhong on 2015/12/24.
 *
 * 非阻塞服务端的创建步骤：
 * 步骤一：打开ServerSocketChannel, 用于监听客户端的链接，它是所有客户端链接的父管道：
 *          ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
 * 步骤二：绑定监听端口，设置链接为非阻塞模式：
 *          serverSocketChannel.bind(new InetSocketAddress(InetAddress.getName("ip"),port));
 *          serverSocketChannel.configureBlocking(false);
 * 步骤三：创建多路复用器，并启用线程：
 *          Selector selectot = Selector.open();
 *          new Thread(new ReactorTask()).start();
 * 步骤四：将ServerSocketChannel注册到多路复用器上，监听ACCEPT事件：
 *          SelectionKey key = serverSocketClannel.register(selectot, SelectionKey.OP_ACCEPT, ioHandler);
 * 步骤五：多路复用器在线程run方法无限循环体内轮询就绪的Key:
 *          int num = Selector.select();
 *          Set selectedKeys = selector.selectedKeys();
 *          Iterator it = selectedKeys.itertaor();
 *          while (it.next()) {
 *              SelectionKey key = (SelectionKey) it.next();
 *              // ... deal with I/O event ...
 *          }
 * 步骤六：多路复用器监听到有新的客户端接入，处理新的接入请求，完成TCP三次握手，建立物理链路：
 *          SocketChannel channel = serverSocketChannel.accept();
 * 步骤七：设置客户端链路为非阻塞模式：
 *          channel.configureBlocking(false);
 *          channel.socket().setReuseAddress(true);
 *          .......
 * 步骤八：将新接入的客户端链接注册到多路复用器上，监听读操作，用来读取客户端发送的网络请求：
 *          SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ, ioHandler);
 * 步骤久：异步读取客户端请求消息到缓冲区：
 *          int readNumber = channel.read(receivedBuffer);
 * 步骤十：对ByteBuffer进行编解码，如果有半包消息指针reset,继续读取后续的报文，将解码成功的消息封装成Task，投递到业务线程池中，进行业务逻辑编排：
 *          Object message = null;
 *          while (buffer.hasRemain()) {
 *              byteBuffer.mark();
 *              Object message = decode(byteBuffer);
 *              if (message == null) {
 *                  byteBuffer.reset();
 *                  break;
 *              }
 *              messageList.add(message);
 *          }
 *          if (!byteBuffer.hasRemain()) {
 *              byteBuffer.clear();
 *          } else {
 *              byteBuffer.compact();
 *          }
 *          if (messageList != null & !messageList.isEmpty()) {
 *              for (Object messageE : messageList) {
 *                  handlerTask(messageE);
 *              }
 *          }
 */
public class NioServer {

    public static void main(String[] args) {
        int port = 9999;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        MultiplexerServer multiplexerServer = new MultiplexerServer(port);
        new Thread(multiplexerServer, "NIO-MultiplexerServer-001").start();
    }
}
