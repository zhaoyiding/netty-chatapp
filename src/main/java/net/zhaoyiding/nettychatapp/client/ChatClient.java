package net.zhaoyiding.nettychatapp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {

	private String host;
	private int port;

	public ChatClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void run() throws Exception {
		NioEventLoopGroup worker = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(worker).channel(NioSocketChannel.class).handler(new ChatClientInitializer());
			Channel channel = b.connect(host, port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				channel.writeAndFlush(in.readLine() + "\r\n");
			}
		} finally {
			worker.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new ChatClient("127.0.0.1", 8080).run();
	}

}
