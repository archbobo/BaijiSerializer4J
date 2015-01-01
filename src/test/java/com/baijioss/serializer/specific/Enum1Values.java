package com.baijioss.serializer.specific;

public enum Enum1Values {

    BLUE(0),
    RED(1),
    GREEN(2);

    private final int value;

    Enum1Values(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Baiji IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Baiji IDL.
     */
    public static Enum1Values findByValue(int value) {
        switch (value) {
            case 0:
                return BLUE;
            case 1:
                return RED;
            case 2:
                return GREEN;
            default:
                return null;
        }
    }
}
