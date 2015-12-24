package cn.hero.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by Fuzhong on 2015/12/24.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AioServerHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AioServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
