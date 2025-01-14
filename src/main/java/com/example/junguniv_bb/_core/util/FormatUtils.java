package com.example.junguniv_bb._core.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
    public static String formatToCurrency(long value) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return "₩" + numberFormat.format(value);
    }
}