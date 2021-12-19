package com.happysnaker.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/7
 * @email happysnaker@foxmail.com
 */
public class VerifyUtils {
    public static List<Integer> string2NumberList(String input) {
        String[] nums = input.split("[,]");
        return Arrays.stream(nums).map((num)->Integer.parseInt(num)).collect(Collectors.toList());
    }

    public static boolean isNumber(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        try {
            Double d = Double.parseDouble(input);
        } catch (Exception e) {
            return false;
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

    public static boolean allIsNumber(String[] inputs1, String... inputs2) {
        return allIsNumber(inputs1) && allIsNumber(inputs2);
    }

    public static boolean isNumberList(String input) {
        if (input == null) {
            return false;
        }
        return isNumber(input.replaceAll("[,]", ""));
    }

    public static boolean allIsNumberList(String... inputs) {
        for (String input : inputs) {
            if (!isNumberList(input)) {
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
