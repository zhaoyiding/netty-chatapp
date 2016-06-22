package net.zhaoyiding.nettychatapp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
		.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
		.addLast("decoder", new StringDecoder())
		.addLast("encoder", new StringEncoder())
		.addLast("handler", new ChatServerHandler());
		
		System.out.println("客户端："+ch.remoteAddress()+"连接上");
	}

}
