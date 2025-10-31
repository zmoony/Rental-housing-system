package com.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * FileUtil
 *
 * @author yuez
 * @since 2025/10/23
 */
public class FileUtil {
    public static void main(String[] args) {
        String path = "E:/个人资料/txt/";
        String srcFileName = "111.txt";
        String destFileName = "111-01.txt";
        addEnter(path,srcFileName,destFileName);
        System.out.println("完成");
    }

    /**
     * 在每一行增加间隙 阅读更加方便
     * 产生新的文件 不改变源文件
     * @param path
     */
    public static void addEnter(String path,String srcFileName,String destFileName) {
        try( Stream<String> lines = Files.lines(Paths.get(path + srcFileName));) {
//            File srcFile = new File(path+srcFileName);
//            File destFile = new File(path+destFileName);
//            Files.copy(srcFile.toPath(), destFile.toPath());
            Files.deleteIfExists(Paths.get(path+destFileName));
            StringBuilder content = new StringBuilder();
            lines.forEach(line -> {
                content.append(line).append("\r\n\r\n"); // 添加行内容并在其后添加两个换行符以创建间隙
            });
            Files.write(Paths.get(path+destFileName), content.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



















