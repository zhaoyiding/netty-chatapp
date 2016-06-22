package net.zhaoyiding.nettychatapp.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		System.out.println("客户端 ：" + ch.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		System.out.println("客户端：" + ch.remoteAddress() + "掉线");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel ch = ctx.channel();
		System.out.println("客户端：" + ch.remoteAddress() + "异常");
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush(ch.remoteAddress() + "加入\n");
		}
		channels.add(ch);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush(ch.remoteAddress() + "离开\n");
		}
		channels.remove(ch);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel ch = ctx.channel();
		for (Channel channel : channels) {
			if (channel != ch) {
				StringBuffer buffer = new StringBuffer("[").append(channel.remoteAddress()).append("]: ").append(msg)
						.append("\n");
				channel.writeAndFlush(buffer.toString());
			} else {
				StringBuffer buffer = new StringBuffer("[you]: ").append(msg).append("\n");
				channel.writeAndFlush(buffer.toString());
			}
		}
	}

}
