package com.fashionstore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtil {

    private PriceUtil() {}

    public static String format(BigDecimal price) {
        if (price == null) {
            return "0.00";
        }
        return price.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
