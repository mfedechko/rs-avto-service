package com.rsavto.categories.data;

import lombok.Getter;

/**
 * @author Mykola Fedechko
 */
@Getter
public enum Category {

    KK(6, "кк", "Клапанні кришки"),
    AKB(11, "акб", "Провід АКБ"),
    PP(8, "пп", "Пневмо подушки"),
    SHP(13, "шп", "патрубки системи охолодження"),
    DK(14, "дк", "Датчик положення кузова"),
    EGR(16, "егр", "Радіатор EGR"),
    TR(17, "тр", "Система охолодження"),
    LZ(26, "лз", "Лямбда-зонд"),
    KL(10, "кл", "Колектор"),
    FG(18, "фг", "Клапан регулювання фаз ГРМ"),
    SHD(19, "шд", "Шестерні, деталі двигуна"),
    ZK(20, "зк", "Замок капоту"),
    ZB(21, "зб", "Замок багажника"),
    EB(22, "еб", "Електрообладнання"),
    BR(23, "бр", "Блоки розжарювання"),
    AKP(24, "акп", "Піддон АКПП"),
    PR(25, "пр", "Система привід"),
    ORIGINAL(27, "ое", "оригінал"),
    RSA(31, "rsa", "RSA"),
    UNKNOWN(-1, "un", "Unknown");

    private final int id;
    private final String category;
    private final String fileName;

    Category(final int id, final String group, final String value) {
        this.id = id;
        this.category = group;
        this.fileName = value;
    }

    public static Category fromAbbr(final String abbr) {
        for (final var group : values()) {
            if (group.getCategory().equals(abbr)) {
                return group;
            }
        }
        return UNKNOWN;
    }
}
