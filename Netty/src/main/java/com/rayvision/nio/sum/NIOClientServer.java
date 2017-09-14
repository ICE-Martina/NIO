package com.rayvision.nio.sum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

/**
 * Author:彭哲
 * Date:2017/9/6
 */
public class NIOClientServer {

    private SocketChannel channel = null;
    private ByteBuffer buffer = ByteBuffer.allocate(8);
    private IntBuffer intBuffer = buffer.asIntBuffer();

    /**
     * 与服务器指定的地址和端口 建立连接
     *
     * @return
     * @throws IOException
     */
    private SocketChannel connect() throws IOException {
        return SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
    }

    /**
     * 发送加法请求到服务器
     *
     * @param a
     * @param b
     * @throws IOException
     */
    public void sendRequest(int a, int b) throws IOException {
        buffer.clear();
        intBuffer.put(0, a);
        intBuffer.put(1, b);
        channel.write(buffer);
        System.out.println("发送加法请求(" + a + "+" + b + ")");
    }

    /**
     * 接收服务器的运算结果
     *
     * @return
     * @throws IOException
     */
    public int receiveResult() throws IOException {
        buffer.clear();
        channel.read(buffer);
        return intBuffer.get(0);
    }

    /**
     * 获得加法运算结果
     *
     * @param a
     * @param b
     * @return
     */
    public int getSum(int a, int b) {
        int result = 0;
        try {
            channel = connect();
            sendRequest(a, b);
            result = receiveResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        int result = new NIOClientServer().getSum(3523, 3545);
        System.out.println("加法运算后的结果为:" + result);
    }


}
