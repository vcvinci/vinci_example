package com.vcvinci.java8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author vinci
 * @Title: FileLocker
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-04-2515:53
 */
public class FileLocker {

    private static String PATH = "/Users/vinci/base_monitor_dove.py";

    public static void main(String[] args) throws IOException {
        File f = new File("aaa.txt");
        System.out.println(getFileContent(f) + 1);// no lock
        FileLock lock = getFileLock(f);// lock
        System.out.println(getFileContent(f) + 2);
        System.out.println(lock.isValid());
        lock.release();// lock release
        System.out.println(lock.isValid());
        System.out.println(getFileContent(f) + 3);// no lock
    }
    /**
     * get file content.
     *
     * @param file
     * @return
     */
    public static String getFileContent(File file) {
        String line = "";
        String content = "";
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(

                    new FileInputStream(file)));
            while ((line = bf.readLine()) != null) {
                content += line;
            }
        } catch (FileNotFoundException e) {
            content = "ERROR ";
        } catch (IOException e) {
            content = "ERROR ";
        }
        return content;
    }
    /**
     * get lock.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static FileLock getFileLock(File file) throws IOException {
        RandomAccessFile fi = new RandomAccessFile(file, "rw");
        FileChannel fc = fi.getChannel();
        return fc.tryLock();
    }
}
