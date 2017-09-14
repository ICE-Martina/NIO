package com.rayvision.nio.time;

/**
 * Author:彭哲
 * Date:2017/9/15
 * NIO 服务端
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        TimeServerHandler timeServerHandler = new TimeServerHandler(port);
        new Thread(timeServerHandler, "TimeServerHandler").start();
    }
}
