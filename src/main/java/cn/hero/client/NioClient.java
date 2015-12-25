package cn.hero.client;

import cn.hero.handler.NioClientHandler;

/**
 * Created by Fuzhong on 2015/12/24.
 *
 * 非阻塞客户端的创建步骤：
 * 步骤一：打开SocketChannel,绑定客户端本地地址（可选，默认系统会随机分配一个可用的本地地址）：
 *          SocketChannel clientChannel = SocketChannel.open();
 * 步骤二：设置SocketChannel为阻塞模式。同时设置客户端链接的TCP参数：
 *          clientChannel.configureBlocking(false);
 *          socket.setReuseAddress(true);
 *          socket.setReceiveBufferSize(BUFFER_SIZE);
 *          socket.setSendBufferSize(BUFFER_SIZE);
 * 步骤三：异步连接服务器：
 *          boolean connected = clientChannel.connect(new InetSocketAddress("ip", port));
 * 步骤四：判断是否连接成功，如果连接成功，则直接注册读状态位到多路复用器中；如果当前没有连接成功（异步连接，返回false，说明客户端已经发
 * 生sync包，服务器没有返回ack包，物理链路还没有建立）：
 *          if (connected) {
 *              clientChannel.register(selector, SelectionKey.OP_READ, ioHandler);
 *          } else {
 *              clientChannel.register(selector, SelectionKey.OP_CONNECT, ioHandler);
 *          }
 * 步骤五：向多路服务器注册OP_CONNMECT状态位，监听服务端的TCP ACK应答：
 *          clientChannel.register(selector, SelectionKey.OP_CONNECT, ioHandler);
 * 步骤六：创建多路复用器想，并启用线程：
 *          Selector selector = Selectot.open();
 *          new Thread(new ReactorTask()).start();
 * 步骤七：多路复用器在线程run方法的无限循环体内轮询准备就绪的Key
 *          int num = selector.select();
 *          Set selectedKeys = selector.selectedKeys();
 *          Iterator it = selectedKeys.iterator();
 *          while (it.hasNext()) {
 *              SelectionKey key = (SelectionKey) it.next();
 *              // ... deal with I/O event ...
 *          }
 * 步骤八：接受connect事件进行处理：
 *          if (key.isConnectable()) {
 *              // handlerConnect();
 *          }
 * 步骤久：判断连接结果，如果连接成功，注册读事件到多路复用器：
 *          if (channel.finishConnect()) {
 *              registerRead();
 *          }
 * 步骤十：注册读事件到多路复用器中：
 *          clientChannel.register(selector, SelectionKey.OP_READ, ioHandler);
 * 步骤十一：异步读客户端请求消息到缓冲区：
 *          int readNumber = channel.read(receiveBuffer);
 * 步骤十二：对ByteBuffer进行编解码。如果有半包消息接收缓冲区reset，继续读取后续的报文，将解码成功的消息封装成Task,投递到业务线程池中，
 * 进行业务逻辑编排：
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
 *          if (messageList != null & !messageList.isEmpty()){
 *              for (Object messageE : messageList) {
 *                  handlerTask(messageE);
 *              }
 *          }
 * 步骤十三：将POJO对象encode成ByteBuffer,调用SocketChannel的异步write接口，将消息异步发送给客户端：
 *         socketChannel.write(buffer);
 */
public class NioClient {

    public static void main(String[] args) {
        int port = 9999;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new Thread(new NioClientHandler("127.0.0.1", port), "").start();
    }

}
