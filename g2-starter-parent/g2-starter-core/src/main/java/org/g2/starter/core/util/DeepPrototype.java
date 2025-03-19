package org.g2.starter.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

/**
 * 对象深拷贝工具
 *
 * @author wenxi.wu@hand-chian.com 2021-04-07
 */
public final class DeepPrototype {

    private static final Logger log = LoggerFactory.getLogger(DeepPrototype.class);

    private DeepPrototype() {
    }

    public static Object deepClone(Objects obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            // 序列化
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);

            // 反序列化
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);

            return ois.readObject();
        } catch (Exception e) {
            log.error("Object Serializable Clone Error :", e);
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                log.error("close stream error:", e);
            }
        }
    }
}
