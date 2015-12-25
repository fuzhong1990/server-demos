package cn.hero.netty.HttpServer;

import cn.hero.netty.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Fuzhong on 2015/12/25.
 */
public class HttpServer {

    private static final String DEFAULT_URL = "/";

    public void bind (int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpRequestDecoder())
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpFileServerHandler(url));
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务器监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            // 退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9999;
        String url = HttpServer.DEFAULT_URL;
        new HttpServer().bind(port, url);
    }
}
