package com.vcvinci.common.util;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.vcvinci.common.exception.common.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 用hessian进行序列化操作  主要用于生产者发送消息的 key 和 value
 * @author vinci
 * @Title HessianUtils
 * @date 2018/11/28下午1:57
 */
public class HessianUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(HessianUtils.class);

    /**
     * 功能描述: 对象转byte数组
     * 使用 Hessian 序列化
     *
     * @param: [obj]
     * @return: byte[]
     * @auther: Administrator
     * @date: 2018/7/13 14:55
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        HessianOutput hessianOutput = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            // Hessian的序列化输出
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error("HessianOutput write to object failed!", e);
            throw new SerializationException();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("ByteArrayInputStream close failed!", e);
            }
            try {
                if (hessianOutput != null) {
                    hessianOutput.close();
                }
            } catch (IOException e) {
                LOGGER.error("HessianOutput close failed!", e);
            }
        }
    }

    /**
     * 功能描述: byte数组转对象
     * 使用 Hessian 反序列化
     * @param bytes 需要反序列化的byte数组
     * @return java.lang.Object
     * @author vinci
     * @date 2018/7/17 11:24
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = null;
        HessianInput hessianInput = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            // Hessian的反序列化读取对象
            hessianInput = new HessianInput(byteArrayInputStream);
            return  hessianInput.readObject();
        } catch (IOException e) {
            LOGGER.error("HessianInput read object failed!", e);
            throw new SerializationException();
        } finally {
            close(byteArrayInputStream, hessianInput);
        }
    }


    public static Object deserialize(ByteBuffer bb) {
        ByteArrayInputStream byteArrayInputStream = null;
        HessianInput hessianInput = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
            // Hessian的反序列化读取对象
            hessianInput = new HessianInput(byteArrayInputStream);
            return  hessianInput.readObject();
        } catch (IOException e) {
            LOGGER.error("HessianInput read object failed!", e);
            throw new SerializationException();
        } finally {
            close(byteArrayInputStream, hessianInput);
        }
    }

    private static void close(ByteArrayInputStream byteArrayInputStream, HessianInput hessianInput) {
        try {
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
        } catch (IOException e) {
            LOGGER.error("ByteArrayInputStream close failed!", e);
        }
        if (hessianInput != null) {
            hessianInput.close();
        }
    }
}
