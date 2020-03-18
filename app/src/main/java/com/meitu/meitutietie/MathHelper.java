package com.meitu.meitutietie;

public class MathHelper {
    public MathHelper() {
    }

    public static double distance(double dx, double dy) {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double distance(double x0, double y0, double x1, double y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        return distance(dx, dy);
    }

    public static double average(double... numbers) {
        if (numbers.length == 0) {
            return 0.0D / 0.0;
        } else {
            double sum = 0.0D;

            for(int i = 0; i < numbers.length; ++i) {
                sum += numbers[i];
            }

            return sum / (double)numbers.length;
        }
    }

    public static double variance(double... numbers) {
        if (numbers.length == 0) {
            return 0.0D / 0.0;
        } else {
            double sum_1 = 0.0D;
            double sum_2 = 0.0D;
            double avr = 0.0D;

            for(int i = 0; i < numbers.length; ++i) {
                sum_1 += numbers[i] * numbers[i];
                sum_2 += numbers[i];
            }

            avr = sum_2 / (double)numbers.length;
            return (sum_1 + (double)numbers.length * avr * avr + 2.0D * avr * sum_2) / (double)numbers.length;
        }
    }

    public static int gcd(int x, int y) {
        int max = Math.abs(Math.max(x, y));
        int min = Math.abs(Math.min(x, y));
        int r = max % min;
        if (r == 0) {
            return min;
        } else {
            while(r != 0) {
                max = min;
                min = r;
                r = max % r;
            }

            return min;
        }
    }

    public static int lcm(int x, int y) {
        int gcd = gcd(x, y);
        return x * y / gcd;
    }

    public static int pixel(float f) {
        if (f < 0.0F) {
            return (int)(f - 0.5F);
        } else {
            return f > 0.0F ? (int)(f + 0.5F) : 0;
        }
    }

    public static int pixel(double d) {
        if (d < 0.0D) {
            return (int)(d - 0.5D);
        } else {
            return d > 0.0D ? (int)(d + 0.5D) : 0;
        }
    }

    public static boolean tryParseBoolean(String string, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(string);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static byte tryParseByte(String string, byte defaultValue) {
        try {
            return Byte.parseByte(string);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static int tryParseInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static long tryParseLong(String string, long defaultValue) {
        try {
            return Long.parseLong(string);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static float tryParseFloat(String string, float defaultValue) {
        try {
            return Float.parseFloat(string);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static double tryParseDouble(String string, double defaultValue) {
        try {
            return Double.parseDouble(string);
        } catch (Exception var4) {
            return defaultValue;
        }
    }
}
