package cn.hero.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.Date;


/**
 * Created by Fuzhong on 2015/12/25.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte [] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("The server receive order : " +body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                new Date(System.currentTimeMillis()).toString() :
                "DAD ORDER";
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
