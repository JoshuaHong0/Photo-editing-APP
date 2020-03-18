package com.meitu.meitutietie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextRW {
    private static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public TextRW() {
    }

    public static void setDefaultCharset(Charset defaultCharset) {
        if (defaultCharset == null) {
            throw new NullPointerException("Param defaultCharset can not be null.");
        } else {
            DEFAULT_CHARSET = defaultCharset;
        }
    }

    public static String readAllText(InputStream inputStream, Charset charset) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[1024];

        int readCount;
        while((readCount = bufferedReader.read(buffer)) > -1) {
            stringBuilder.append(buffer, 0, readCount);
        }

        return stringBuilder.toString();
    }

    public static String readAllText(InputStream inputStream) throws IOException {
        return readAllText(inputStream, DEFAULT_CHARSET);
    }

    public static String readAllText(File file, Charset charset) throws IOException {
        FileInputStream fileInputStream = null;

        String var3;
        try {
            fileInputStream = new FileInputStream(file);
            var3 = readAllText((InputStream)fileInputStream, charset);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception var10) {
                }
            }

        }

        return var3;
    }

    public static String readAllText(File file) throws IOException {
        return readAllText(file, DEFAULT_CHARSET);
    }

    public static void writeAllText(OutputStream outputStream, Charset charset, String content) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        outputStreamWriter.write(content);
        outputStreamWriter.flush();
    }

    public static void writeAllText(OutputStream outputStream, String content) throws IOException {
        writeAllText(outputStream, DEFAULT_CHARSET, content);
    }

    public static void writeAllText(File file, Charset charset, String content) throws IOException {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            writeAllText((OutputStream)fileOutputStream, charset, content);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception var10) {
                }
            }

        }

    }

    public static void writeAllText(File file, String content) throws IOException {
        writeAllText(file, DEFAULT_CHARSET, content);
    }

    public static List<String> readAllLines(InputStream inputStream, Charset charset) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ArrayList lines = new ArrayList();

        String tempString;
        while((tempString = bufferedReader.readLine()) != null) {
            lines.add(tempString);
        }

        return lines;
    }

    public static List<String> readAllLines(InputStream inputStream) throws IOException {
        return readAllLines(inputStream, DEFAULT_CHARSET);
    }

    public static List<String> readAllLines(File file, Charset charset) throws IOException {
        FileInputStream fileInputStream = null;

        List var3;
        try {
            fileInputStream = new FileInputStream(file);
            var3 = readAllLines((InputStream)fileInputStream, charset);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception var10) {
                }
            }

        }

        return var3;
    }

    public static List<String> readAllLines(File file) throws IOException {
        return readAllLines(file, DEFAULT_CHARSET);
    }

    public static void writeAllLines(OutputStream outputStream, Charset charset, List<String> content) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        Iterator var4 = content.iterator();

        while(var4.hasNext()) {
            String line = (String)var4.next();
            outputStreamWriter.write(line);
            outputStreamWriter.write(10);
        }

        outputStreamWriter.flush();
    }

    public static void writeAllLines(OutputStream outputStream, List<String> content) throws IOException {
        writeAllLines(outputStream, DEFAULT_CHARSET, content);
    }

    public static void writeAllLines(File file, Charset charset, List<String> content) throws IOException {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            writeAllLines((OutputStream)fileOutputStream, charset, content);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception var10) {
                }
            }

        }

    }

    public static void writeAllLines(File file, List<String> content) throws IOException {
        writeAllLines(file, DEFAULT_CHARSET, content);
    }
}

