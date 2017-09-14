package com.rayvision.nio.sum;

import java.nio.IntBuffer;

/**
 * Author:彭哲
 * Date:2017/9/6
 */
public class BufferTest {

    public static void main(String[] args) {
        //创建指定长度的缓冲区
        IntBuffer intBuffer = IntBuffer.allocate(10);
        //对缓冲区某个位置上的元素进行修改
        intBuffer.put(0, 7);
        System.out.println("缓冲区数据如下:");
        IntBuffer duplicate = intBuffer.duplicate();
        for (int i = 0; i < intBuffer.limit(); i++) {
            System.out.println(intBuffer.get() + "\t");
        }

    }

}
