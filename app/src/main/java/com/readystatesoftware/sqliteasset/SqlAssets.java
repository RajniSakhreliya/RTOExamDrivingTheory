package com.readystatesoftware.sqliteasset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class SqlAssets {

    public static List<String> list(String str, char c) {
        List<String> arrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        char[] charArray = str.toCharArray();
        boolean z = false;
        for (int i = 0; i < str.length(); i++) {
            if (charArray[i] == '\"') {
                z = !z;
            }
            if (charArray[i] != c || z) {
                sb.append(charArray[i]);
            } else if (sb.length() > 0) {
                arrayList.add(sb.toString().trim());
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            arrayList.add(sb.toString().trim());
        }
        return arrayList;
    }

    public static void getAsset(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read > 0) {
                    outputStream.write(bArr, 0, read);
                } else {
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    return;
                }
            }
        } catch (Exception ignored) {

        }
    }

    public static ZipInputStream zipInputStream(InputStream inputStream) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry nextEntry = null;
        try {
            nextEntry = zipInputStream.getNextEntry();
        } catch (IOException ignored) {
        }
        if (nextEntry == null) {
            return null;
        }
        return zipInputStream;
    }

    public static String b(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }
}
