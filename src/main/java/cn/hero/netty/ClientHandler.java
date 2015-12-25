package cn.hero.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

/**
 * Created by Fuzhong on 2015/12/25.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    public static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final ByteBuf firstMessage;

    public ClientHandler () {
        byte [] request = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(request.length);
        firstMessage.writeBytes(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte [] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String body = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception form downstream : "+cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }
}
