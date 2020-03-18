package com.meitu.meitutietie;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemCache {
    private static final Map<String, SoftReference<Object>> MAP = new ConcurrentHashMap();

    public MemCache() {
    }

    public static <T> void put(String key, T object) {
        MAP.put(key, new SoftReference(object));
    }

    public static <T> T get(String key) {
        SoftReference<Object> result = (SoftReference)MAP.remove(key);
        if (result == null) {
            return null;
        } else {
            Object object = result.get();
            return object == null ? null : (T) object;
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        Boolean result = (Boolean)get(key);
        return result == null ? defValue : result;
    }

    public static byte getByte(String key, byte defValue) {
        Byte result = (Byte)get(key);
        return result == null ? defValue : result;
    }

    public static short getShort(String key, short defValue) {
        Short result = (Short)get(key);
        return result == null ? defValue : result;
    }

    public static char getChar(String key, char defValue) {
        Character result = (Character)get(key);
        return result == null ? defValue : result;
    }

    public static float getFloat(String key, float defValue) {
        Float result = (Float)get(key);
        return result == null ? defValue : result;
    }

    public static int getInt(String key, int defValue) {
        Integer result = (Integer)get(key);
        return result == null ? defValue : result;
    }

    public static double getDouble(String key, double defValue) {
        Double result = (Double)get(key);
        return result == null ? defValue : result;
    }

    public static long getLong(String key, long defValue) {
        Long result = (Long)get(key);
        return result == null ? defValue : result;
    }
}
