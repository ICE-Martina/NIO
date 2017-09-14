package com.rayvision.bio.time;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author:彭哲
 * Date:2017/9/13
 * 传统Socket服务端
 */
public class TimeServer {


    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认端口8080
            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start on port:" + port);
            Socket socket = null;
            //死循环监听客户端的连接,在没有客户端接入时,主线程一直阻塞在accept方法上
            while (true) {
                socket = serverSocket.accept();
                //新启一个线程处理接入的socket
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("The time server closed");
                serverSocket = null;
            }
        }


    }
}
