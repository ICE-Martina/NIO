package com.rayvision.nio.sum;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Author:彭哲
 * Date:2017/9/5
 */
public class NIOServer {

//    private static Logger LOGGER = LoggerFactory.getLogger(NIOServer.class);


    private int flag = 1;
    //缓冲区大小
    private int blockSize = 4096;
    //发送数据的缓冲区
    private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    //接收数据的缓冲区
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);
    //多路复用器
    private Selector selector;

    public NIOServer(int port) throws IOException {
        //打开服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置服务端是否阻塞
        serverSocketChannel.configureBlocking(false);
        //获得ServerSocket（进行服务端Socket通信）
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定服务端socket的端口
        serverSocket.bind(new InetSocketAddress(port));
        //打开多路复用器
        selector = Selector.open();
        //把服务端Channel注册到多路复用器(注册Accept事件来接受客户端的连接)
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        LOGGER.info("Server start on" + port);
        System.out.println("Server start on"+port);
    }

    //不断监听客户端连接
    public void listen() throws IOException {
        while (true) {
            //不断轮询选择器,获得事件列表(客户端跟服务端都需注册到选择器上)
            selector.select();
            //获得事件列表集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                //业务逻辑处理
                handlerKey(selectionKey);
            }
        }
    }

    private void handlerKey(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        //接收到客户端的数据
        String receiveData;
        //发送到客户端的数据
        String sentData;
        int count;
        //判断事件类型（Accept,监听事件）
        if (selectionKey.isAcceptable()) {
            //获得ServerSocketChannel(服务端Channel)
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            //获得SocketChannel(客户端的Channel)
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            //注册客户端Channel的读事件
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            //将客户端发送的数据读到缓冲区
            count = socketChannel.read(receiveBuffer);
            if (count > 0) {
                receiveData = new String(receiveBuffer.array(), 0, count);
//                LOGGER.info("服务端接收到客户端的数据:" + receiveData);
                //注册客户端写事件
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            sendBuffer.clear();
            //获得客户端Channel
            socketChannel = (SocketChannel) selectionKey.channel();
            //发送到客户端的数据
            sentData = "message send to client from server" + flag++;
            sendBuffer.put(sentData.getBytes());
            sendBuffer.flip();
            //发送数据到客户端
            socketChannel.write(sendBuffer);
//            LOGGER.info("服务端发送数据给客户端:" + sentData);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        NIOServer nioServer = new NIOServer(port);
        nioServer.listen();
    }

}
