package com.example.bellashdefinder.util;

import java.text.DecimalFormat;

public class NumberUtil {

    public static double getOneDigit(double value) {
        return Double.valueOf(new DecimalFormat("#.#").format(Math.round(value * 2) / 2d));
    }

    public static double convertPercentToAmount(double percent, double total) {
        return total * percent / 100;
    }
}
