package com.meitu.meitutietie;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class AssetsHelper {
    public AssetsHelper() {
    }

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    public static String readTextFile(Context context, String name, Charset charset) {
        AssetManager manager = context.getAssets();

        try {
            InputStream stream = manager.open(name);
            String text = TextRW.readAllText(stream, charset);
            stream.close();
            return text;
        } catch (IOException var6) {
            return null;
        }
    }

    public static String readTextFile(Context context, String name) {
        return readTextFile(context, name, Charset.defaultCharset());
    }

    public static Bitmap readImageFile(Context context, String name, Rect outPadding, BitmapFactory.Options opts) {
        AssetManager manager = null;
        InputStream stream = null;

        Object var7;
        try {
            manager = context.getAssets();
            stream = manager.open(name);
            Bitmap var6 = BitmapFactory.decodeStream(stream, outPadding, opts);
            return var6;
        } catch (IOException var17) {
            var7 = null;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception var16) {
            }

        }

        return (Bitmap)var7;
    }

    public static Bitmap readImageFile(Context context, String name) {
        return readImageFile(context, name, (Rect)null, (BitmapFactory.Options)null);
    }
}
