package com.vcvinci.python;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author vinci
 * @Title: Executor
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-03-2814:14
 */
public class Executor {
    private static String PATH = "/Users/vinci/source/cache/base_monitor.py";

    public static void main(String[] args) {
        // step 1: 拷贝resource目录下的 python 脚本到 指定的 PATH
        copyToSystemOfPy();
        // step 2: 从指定的PATH 中获取脚本并执行
        execute();

    }

    private static void copyToSystemOfPy() {
        try (InputStream input = Objects.requireNonNull(Executor.class.getClassLoader().getResourceAsStream("base_monitor.py"));
             OutputStream output = new FileOutputStream(PATH)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute() {
        String command = "python " + PATH + " /usr /bin";
        Process proc;
        try {
            // 执行py文件
            proc = Runtime.getRuntime().exec(command);
            // 用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            FileOutputStream output = new FileOutputStream("/Users/vinci/bin/data/result.text", true);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                line += "\n";
                output.write(line.getBytes(), 0, line.length());
            }
            output.close();
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
