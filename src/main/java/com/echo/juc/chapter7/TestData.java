package com.echo.juc.chapter7;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 生成测试数据
 */
@Slf4j(topic = "c.TestData")
public class TestData {
    static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        int length = ALPHA.length();
        int count = 200;
        //生成一个初始容量为 200 * 26的集合
        ArrayList<String> list = new ArrayList<>(length * count);
        //将26个字母，每个字母重复200次，存入集合中
        for (int i = 0; i < length; i++) {
            char c = ALPHA.charAt(i);
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(c));
            }
        }
        //将集合打乱
        Collections.shuffle(list);

        for (int i = 0; i < 26; i++) {{
            //开启一个输出流
            try(PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("tmp/" + (i + 1) + ".txt")
                    )
            )) {
                //将集合中的元素每200个一组，每个元素后面加个\n，拿出来
                String collect = list.subList(i * count, (i + 1) * count).stream().collect(Collectors.joining("\n"));
                //写到文件中
                out.println(collect);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        }
    }
}
