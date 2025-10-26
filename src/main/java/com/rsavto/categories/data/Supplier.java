package com.rsavto.categories.data;

import com.rsavto.categories.exception.UnknownSupplierException;

/**
 * @author Mykola Fedechko
 */
public enum Supplier {

    PLANETA(true, 8, 116, "Planeta"),
    AVD(true, 14, 253, "AVD"),
    LIDER(true, 20, 267, "АВТО ЛІДЕР KYIV"),
    SYNDICAR(true, 10, 145, "Syndicar"),
    MASTERTEILE(true, 11, 265, "master"),
    UTR(true, 6, 123, "UTR"),
    BUS_ALL(false, 12, 249, "Бус"),
    BUS_LVIV(false, 13, 248, "Бус Маркет Львів"),
    ASG(true, 24, 269, "ASG Пустомити Київ"),
    BASTION(true, 9, 254, "Бастіон"),
    OMEGA_ALL(true, 28, 153, "Омега"),
    OMEGA_LVIV(true, 27, 197, "Омега Львів"),
    AVTONOVA_ALL(true, 22, 99, "АВТОНОВА"),
    AVTONOVA_LVIV(true, 21, 268, "АВТОНОВА Львів"),
    SKLAD_KYIV(true, 2, 213, "Склад Київ"),
    SKLAD_PLASTOVA(true, 3, 55, "Пластова"),
    SCLASS(false, 7, 271, "S Class"),
    VLADISLAV_ALL(true, 15, 159, "Vladislav"),
    VLADISLAV_LVIV(true, 33, 158, "Vladislav Lviv"),
    ELIT_ALL(true, 17, 69, "Elit"),
    ELIT_LVIV(true, 16, 67, "Elit Lviv"),
    INTERCARS_ALL(true, 34, 272, "Inter Cars"),
    INTERCARS_LVIV(true, 32, 104, "Inter Cars Lviv"),
    TECHNOMIR(false, 37, 275, "Tehnomir Parts"),
    TECHNOMIR_OIL(false, 38, 276, "Tehnomir Oil"),
    AVTODIM(true, 39, 168, "Avtodim");

    private final boolean enabled;
    private final int rsAvtoId;
    private final int fortunaId;
    private final String value;

    Supplier(final boolean enabled, final int rsAvtoId, final int fortunaId, final String value) {
        this.enabled = enabled;
        this.rsAvtoId = rsAvtoId;
        this.fortunaId = fortunaId;
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getRsAvtoId() {
        return rsAvtoId;
    }

    public int getFortunaId() {
        return fortunaId;
    }

    public String getValue() {
        return value;
    }

    public static Supplier fromName(final String value) {
        for (final var supplier : values()) {
            if (supplier.name().equalsIgnoreCase(value)) {
                return supplier;
            }
        }
        throw new UnknownSupplierException("Unknown supplier " + value);
    }

    public static Supplier fromRsAvtoId(final int id) {
        for (final var supplier : values()) {
            if (supplier.getRsAvtoId() == id) {
                return supplier;
            }
        }
        throw new UnknownSupplierException("Unknown supplier " + id);
    }

    public static Supplier fromFortunaAvtoId(final int id) {
        for (final var supplier : values()) {
            if (supplier.getFortunaId() == id) {
                return supplier;
            }
        }
        throw new UnknownSupplierException("Unknown supplier");
    }

    public static Supplier fromTextValue(final String supplierTextValue) {
        for (final var supplier : values()) {
            if (supplier.getValue().equalsIgnoreCase(supplierTextValue)) {
                return supplier;
            }
        }
        throw new UnknownSupplierException("Unknown supplier");
    }


    public String toLowerCase() {
        return name().toLowerCase();
    }
}
