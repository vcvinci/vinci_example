package com.vcvinci.common.util;

import com.vcvinci.common.schema.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutput;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static final Pattern HOST_PORT_PATTERN = Pattern.compile(".*?\\[?([0-9a-zA-Z\\-%._:]*)\\]?:([0-9]+)");

    /**
     * 序列化 Struct 对象
     *
     * @param struct
     * @return
     */
    public static ByteBuffer serialize(Struct struct, short version) {
        // 在首位加上2字节的版本号
        ByteBuffer buffer = ByteBuffer.allocate(2 + struct.sizeOf());
        buffer.putShort(version);
        struct.writeTo(buffer);
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer serialize(Struct struct) {
        ByteBuffer buffer = ByteBuffer.allocate(struct.sizeOf());
        struct.writeTo(buffer);
        buffer.flip();
        return buffer;
    }



    /**
     * 功能描述: byte数组转对象
     * 使用 Hessian 反序列化
     *
     * @param: [bytes]
     * @return: java.lang.Object
     * @auther: Administrator
     * @date: 2018/7/17 11:24
     */
    public static Object deserialize(byte[] bytes) {
        // TODO 带实现
        return new Object();
    }

    /**
     * 功能描述:从“主机:端口”地址字符串中提取主机名。
     *
     * @param: [address]
     * @return: java.lang.String
     * @author vinci
     * @date: 2018/6/25 14:35
     */
    public static String getHost(String address) {
        Matcher matcher = HOST_PORT_PATTERN.matcher(address);
        return matcher.matches() ? matcher.group(1) : null;
    }

    /**
     * 功能描述:从“主机:端口”地址字符串中提取端口号。
     *
     * @param: [address]
     * @return: java.lang.Integer
     * @auther: Administrator
     * @date: 2018/6/25 14:36
     */
    public static Integer getPort(String address) {
        Matcher matcher = HOST_PORT_PATTERN.matcher(address);
        return matcher.matches() ? Integer.parseInt(matcher.group(2)) : null;
    }

    /**
     * 校验输入的ip是本服务器的
     *
     * @param ip
     */
    public static void validateIp(String ip) {
        List<String> ipList = getLocalIPList();
        if (!ipList.contains(ip)) {
            throw new IllegalArgumentException(String.format("%s 不是此服务器的ip", ip));
        }
    }

    /**
     * 获取本机所有ip
     *
     * @return
     */
    private static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    // IPV4
                    if (inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            LOGGER.error("获取本机所有ip出错", e);
        }
        return ipList;
    }

    /**
     * 功能描述:将主机名和端口号格式化为“主机:端口”地址字符串，
     * 使用大括号'['，']'包围IPv6地址
     *
     * @param: [host, port]
     * @return: java.lang.String
     * @auther: Administrator
     * @date: 2018/6/25 14:37
     */
    public static String formatAddress(String host, Integer port) {
        return host.contains(":")
                ? "[" + host + "]:" + port // IPv6
                : host + ":" + port;
    }

    /**
     * 功能描述:通过className 实例化类
     *
     * @param: [className, t]
     * @return: T
     * @author vinci
     * @date: 2018/6/25 14:38
     */
    public static <T> T getInstanceByClassName(String className, Class<T> t) {
        Class<?> c;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Class not found by name " + className, e);
            return null;
        }
        if (c == null) {
            return null;
        }
        Object o = Utils.newInstance(c);
        if (!t.isInstance(o)) {
            LOGGER.error(c.getName() + " is not an instance of " + t.getName());
            return null;
        }
        return t.cast(o);
    }

    /**
     * 实例化类
     */
    public static <T> T newInstance(Class<T> c) {
        if (c == null) {
            throw new NullPointerException("class cannot be null");
        }
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            LOGGER.error("Could not find a public no-argument constructor for " + c.getName(), e);
            return null;
        } catch (ReflectiveOperationException | RuntimeException e) {
            LOGGER.error("Could not instantiate class " + c.getName(), e);
            return null;
        }
    }

    public static byte[] utf8(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static int utf8Length(CharSequence s) {
        int count = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            char ch = s.charAt(i);
            if (ch <= 0x7F) {
                count++;
            } else if (ch <= 0x7FF) {
                count += 2;
            } else if (Character.isHighSurrogate(ch)) {
                count += 4;
                ++i;
            } else {
                count += 3;
            }
        }
        return count;
    }

    public static String utf8(ByteBuffer buffer, int length) {
        return utf8(buffer, 0, length);
    }

    public static String utf8(ByteBuffer buffer, int offset, int length) {
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset()
                    + buffer.position() + offset, length, StandardCharsets.UTF_8);
        } else {
            return utf8(toArray(buffer, offset, length));
        }
    }

    public static String utf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] toArray(ByteBuffer buffer, int offset, int size) {
        byte[] dest = new byte[size];
        if (buffer.hasArray()) {
            System.arraycopy(buffer.array(),
                    buffer.position() + buffer.arrayOffset() + offset, dest, 0,
                    size);
        } else {
            int pos = buffer.position();
            buffer.position(pos + offset);
            buffer.get(dest);
            buffer.position(pos);
        }
        return dest;
    }

    /**
     * 将给定的字节缓冲区从当前位置读取到其限制为字节数组。
     * @param buffer 要读取的缓冲区
     */
    public static byte[] toArray(ByteBuffer buffer) {
        return toArray(buffer, 0, buffer.remaining());
    }

    /**
     * 将缓冲区的内容写入输出流。从缓冲区中的当前位置复制字节。
     * @param out 要写入的输出
     * @param buffer 要写入的缓冲区
     * @param length 要写入的字节数
     * @throws IOException 对于写入输出的任何错误
     */
    public static void writeTo(DataOutput out, ByteBuffer buffer, int length) throws IOException {
        if (buffer.hasArray()) {
            out.write(buffer.array(), buffer.position() + buffer.arrayOffset(), length);
        } else {
            int pos = buffer.position();
            for (int i = pos; i < length + pos; i++) {
                out.writeByte(buffer.get(i));
            }
        }
    }
}
