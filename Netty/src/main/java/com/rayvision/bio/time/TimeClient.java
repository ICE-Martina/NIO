package com.rayvision.bio.time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Author:彭哲
 * Date:2017/9/14
 * 传统Socket客户端
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认端口
            }
        }
        Socket client = null;
        //用来接收服务端发送到客户端的流对象
        BufferedReader in = null;
        //客户端发送到服务端的流对象
        PrintWriter out = null;
        try {
            client = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            //像服务端发送数据
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server success");
            //服务端响应
            String response = in.readLine();
            System.out.println("Now is:" + response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out!=null) {
                out.close();
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client = null;
            }
        }

    }
}
