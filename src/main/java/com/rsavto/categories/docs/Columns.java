package com.rsavto.categories.docs;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Mykola Fedechko
 */
@UtilityClass
public class Columns {

    public static final Map<String, Integer> INPUT_COLUMNS = createInputColumnsMap();
    public static final Map<String, Integer> RSA_COLUMNS = createRsaColumnsMap();
    public static final Map<String, Integer> PRICE_COLUMNS = createPriceColumnsMap();
    public static final Map<String, Integer> INPUT_FULL = createInputFullColumnsMap();

    private static final int A = 0;
    private static final int B = 1;
    private static final int C = 2;
    private static final int D = 3;
    private static final int E = 4;
    private static final int F = 5;
    private static final int G = 6;
    private static final int H = 7;
    private static final int I = 8;
    private static final int J = 9;
    private static final int K = 10;
    private static final int L = 11;
    private static final int M = 12;
    private static final int N = 13;

    public static final String NUMBER = "number";
    public static final String ARTICLE = "article";
    public static final String BRAND = "brand";
    public static final String DESCRIPTION = "description";
    public static final String GROUP = "group";
    public static final String AVTOPRO_PRICE = "avtopro_price";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String PRICE_P = "price_p";
    public static final String PRICE_G = "price_g";

    public static final String CATEGORY = "category";
    public static final String PRICE1 = "price1";
    public static final String PRICE2 = "price2";
    public static final String BX = "BX";
    public static final String SKLAD = "sklad";
    public static final String COMMENT1 = "comment1";
    public static final String PHOTO = "photo";
    public static final String COMMENT2 = "comment2";
    public static final String COMMENT3 = "comment3";

    private static Map<String, Integer> createInputColumnsMap() {
        return Map.of(
                BRAND, B,
                ARTICLE, C,
                DESCRIPTION, D,
                GROUP, E,
                AVTOPRO_PRICE, F,
                PRICE, G,
                QUANTITY, I);
    }

    private static Map<String, Integer> createRsaColumnsMap() {
        return Map.of(
                BRAND, B,
                ARTICLE, C,
                DESCRIPTION, D,
                GROUP, E,
                AVTOPRO_PRICE, F,
                PRICE, G,
                QUANTITY, I);
    }

    private static Map<String, Integer> createPriceColumnsMap() {
        return Map.of(
                BRAND, A,
                ARTICLE, B,
                DESCRIPTION, C,
                PRICE_P, D,
                PRICE_G, E,
                QUANTITY, F);
    }

    private static Map<String, Integer> createInputFullColumnsMap() {
        return Map.ofEntries(
                entry(NUMBER, A),
                entry(BRAND, B),
                entry(ARTICLE, C),
                entry(DESCRIPTION, D),
                entry(CATEGORY, E),
                entry(PRICE1, F),
                entry(PRICE2, G),
                entry(BX, H),
                entry(QUANTITY, I),
                entry(SKLAD, J),
                entry(COMMENT1, K),
                entry(PHOTO, L),
                entry(COMMENT2, M),
                entry(COMMENT3, N)
        );
    }
}
