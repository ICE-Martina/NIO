package com.rayvision.nio.sum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Author:彭哲
 * Date:2017/9/6
 */
public class NIOChannelServer {

    //用于发送或接收数据
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //创建一个int缓冲区的试图 此缓冲区内容的更改在新缓冲区中是可见的,反之亦然
    private IntBuffer intBuffer = byteBuffer.asIntBuffer();
    private SocketChannel clientChannel = null;
    private ServerSocketChannel serverChannel = null;

    /**
     * 打开服务端的通道
     *
     * @throws IOException
     */
    public void openChannel() throws IOException {
        //建立一个新的连接的通道
        serverChannel = ServerSocketChannel.open();
        //为新的通道设置访问的端口
        serverChannel.socket().bind(new InetSocketAddress(8888));
        System.out.println("服务器通道已打开");
    }

    /**
     * 等待客户端连接请求
     *
     * @throws IOException
     */
    public void waitReqConnnection() throws IOException {
        while (true) {
            clientChannel = serverChannel.accept();
            if (null != clientChannel) {
                System.out.println("新的连接加入");
            }
            //处理数据
            processReq();
            clientChannel.close();

        }
    }

    /**
     * 处理客户端请求过来的数据
     *
     * @throws IOException
     */
    public void processReq() throws IOException {
        System.out.println("开始读取和处理客户端数据");
        //把当前位置设置为0,上限值修改为容量的值,做好读取数据的准备
        byteBuffer.clear();
        clientChannel.read(byteBuffer);
        int result = intBuffer.get(0) + intBuffer.get(1);
        byteBuffer.flip();
        byteBuffer.clear();
        //修改视图,原来的缓冲区也会变
        intBuffer.put(0, result);
        clientChannel.write(byteBuffer);
        System.out.println("读取和处理客户端数据完成");
    }

    public void start() {
        try {
            //打开服务通道
            openChannel();
            //等待客户端连接
            waitReqConnnection();
            clientChannel.close();
            System.out.println("服务端处理完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NIOChannelServer().start();
    }
}
