package io.zhile.research.intellij.ier.common;

import com.intellij.openapi.util.io.FileUtil;
import io.zhile.research.intellij.ier.helper.DateTime;

import java.io.*;
import java.util.Date;

public class LicenseFileRecord implements EvalRecord {
    private final String type = "LICENSE";
    private final File file;
    private final Date expireDate;

    public LicenseFileRecord(File file) {
        this.file = file;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            expireDate = new Date(~dis.readLong() + 2592000000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() throws Exception {
        if (!FileUtil.delete(file)) {
            throw new Exception("Remove " + type + " failed: " + file.getAbsolutePath());
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeLong(~System.currentTimeMillis());
        }
    }

    @Override
    public String toString() {
        return type + ": " + file.getName() + ", UNTIL: " + DateTime.DF_DATETIME.format(expireDate);
    }
}
