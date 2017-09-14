package com.rayvision.nio.time;

/**
 * Author:彭哲
 * Date:2017/9/15
 * NIO 客户端
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8081;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        new Thread(new TimeClientHandler("127.0.0.1", port),"TimeClientHandler").start();
    }
}
