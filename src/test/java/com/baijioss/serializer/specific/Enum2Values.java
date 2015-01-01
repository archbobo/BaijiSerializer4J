package com.baijioss.serializer.specific;

public enum Enum2Values {

    CAR(0),
    BIKE(1),
    PLANE(2);

    private final int value;

    Enum2Values(int value) {
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
    public static Enum2Values findByValue(int value) {
        switch (value) {
            case 0:
                return CAR;
            case 1:
                return BIKE;
            case 2:
                return PLANE;
            default:
                return null;
        }
    }
}
