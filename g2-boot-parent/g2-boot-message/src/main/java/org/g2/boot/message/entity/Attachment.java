package org.g2.boot.message.entity;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class Attachment {

    private byte[] file;
    private String fileName;

    public byte[] getFile() {
        return file;
    }

    public Attachment setFile(byte[] file) {
        this.file = file;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Attachment setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attachment that = (Attachment) o;
        return Arrays.equals(file, that.file) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fileName);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }
}
