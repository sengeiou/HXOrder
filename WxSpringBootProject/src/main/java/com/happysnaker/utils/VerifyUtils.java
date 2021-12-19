package com.happysnaker.utils;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/7
 * @email happysnaker@foxmail.com
 */
public class VerifyUtils {

    /**
     * 十进制转换成 to 进制
     * @param val 10进制数 val > 0
     * @param to 要转换的进制，2 <= to <= 32
     * @return
     */
    public static String BaseConversion(long val, int to) {
        StringBuilder sb = new StringBuilder();
        do {
            long mod =  val % to;
            if (mod >= 10) {
                sb.append((char) ('A' + mod - 10));
            } else {
                sb.append(mod);
            }
            val /= to;
        } while (val != 0);
        return sb.reverse().toString();
    }


    public static boolean isNumber(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean allIsNumber(String... inputs) {
        for (String input : inputs) {
            if (!isNumber(input)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean partOfIsNullOrEmpty(String... inputs) {
        for (String input : inputs) {
            if (isNullOrEmpty(input)) {
                return true;
            }
        }
        return false;
    }
}
