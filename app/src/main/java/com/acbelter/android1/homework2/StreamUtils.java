package com.acbelter.android1.homework2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        while (true) {
            int r = in.read(buffer);
            if (r == -1) {
                break;
            }
            out.write(buffer, 0, r);
        }
    }

    public static String readStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[8192];

        while ((read = in.read(data, 0, data.length)) != -1) {
            out.write(data, 0, read);
        }

        out.flush();
        return out.toString("utf-8");
    }
}
