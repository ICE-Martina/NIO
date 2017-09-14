package com.rayvision.nio.sum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Author:彭哲
 * Date:2017/9/5
 */
public class NIOClient {

    private static int flag = 1;
    private static int blockSize = 4096;
    //发送数据的缓冲区
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    //接收数据的缓冲区
    private static ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);
    //服务端的InetSocketAddress
    private final static InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 8080);

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //打开选择器(NIO主要在与选择器交互)
        Selector selector = Selector.open();
        //注册连接事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        //连接服务端
        socketChannel.connect(serverAddress);
        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterable;
        //事件列表的某一个Key
        SelectionKey selectionKey;
        SocketChannel client;
        String receiveData;
        String sendData;
        int count;
        while (true) {
            selectionKeys = selector.selectedKeys();
            iterable = selectionKeys.iterator();
            while (iterable.hasNext()) {
                selectionKey = iterable.next();
                if (selectionKey.isConnectable()) {
                    System.out.println("客户端发起连接");
                    client = (SocketChannel) selectionKey.channel();
                    //完成连接
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        System.out.println("客户端完成连接操作");
                        sendBuffer.clear();
                        sendBuffer.put("Hello,NIO".getBytes());
                        sendBuffer.flip();
                        client.write(sendBuffer);
                    }
                    //注册客户端的读事件
                    client.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    client = (SocketChannel) selectionKey.channel();
                    receiveBuffer.clear();
                    count = client.read(receiveBuffer);
                    if (count > 0) {
                        receiveData = new String(receiveBuffer.array(), 0, count);
                        System.out.println("客户端接受到服务端的数据:" + receiveData);
                        //注册写事件
                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                } else if (selectionKey.isWritable()) {
                    sendBuffer.clear();
                    client = (SocketChannel) selectionKey.channel();
                    sendData = "Message send to server--->" + flag++;
                    sendBuffer.put(sendData.getBytes());
                    sendBuffer.flip();
                    client.write(sendBuffer);
                    System.out.println("客户端发送数据给服务端" + sendData);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }
    }
}
