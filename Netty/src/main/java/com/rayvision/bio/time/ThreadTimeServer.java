package com.rayvision.bio.time;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author:彭哲
 * Date:2017/9/14
 * 伪异步IO
 */
public class ThreadTimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start in port:" + port);
            Socket socket = null;
            //创建IO任务线程池
            TimeServerHandlerExecutePool singerExecutor =
                    new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = serverSocket.accept();
                singerExecutor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                System.out.println("The time server close");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
