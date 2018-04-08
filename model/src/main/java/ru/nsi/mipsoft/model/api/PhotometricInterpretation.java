package ru.nsi.mipsoft.model.api;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum PhotometricInterpretation {

    MONOCHROME1("MONOCHROME1"),
    MONOCHROME2("MONOCHROME2"),
    PALETTE_COLOR("PALETTE COLOR"),
    RGB("RGB"),
    HSV("HSV"),
    ARGB("ARGB"),
    CMYK("CMYK"),
    YBR_FULL("YBR_FULL"),
    YBR_FULL_422("YBR_FULL_422"),
    YBR_PARTIAL_422("YBR_PARTIAL_422"),
    YBR_PARTIAL_420("YBR_PARTIAL_420"),
    YBR_ICT("YBR_ICT"),
    YBR_RCT("YBR_RCT"),
    UNKNOWN("UNKNOWN");

    PhotometricInterpretation(String stringValue) {
        this.stringValue = stringValue;
    }

    public boolean isEqual(String stringValue) {
        return stringValue.toUpperCase().equals(stringValue);
    }

    public static PhotometricInterpretation getInstanceFromString(final String stringValue) {
        PhotometricInterpretation[] values = values();
        Optional<PhotometricInterpretation> optional = Arrays.stream(values).filter(
                new Predicate<PhotometricInterpretation>() {
                    @Override
                    public boolean test(PhotometricInterpretation photometricInterpretation) {
                        return photometricInterpretation.isEqual(stringValue);
                    }
                }
        ).findFirst();
        return optional.isPresent() ? optional.get() : PhotometricInterpretation.UNKNOWN;
    }

    public static boolean isBelongToValidValues(final String stringValue, final PhotometricInterpretation...validValues){
        PhotometricInterpretation instanceFromString = getInstanceFromString(stringValue);
        return isBelongToValidValues(instanceFromString, validValues);
    }

    public static boolean isBelongToValidValues(final PhotometricInterpretation value, final PhotometricInterpretation...validValues) {
        return Arrays.stream(validValues).anyMatch(new Predicate<PhotometricInterpretation>() {
            @Override
            public boolean test(PhotometricInterpretation photometricInterpretation) {
                return photometricInterpretation == value;
            }
        });
    }


    private final String stringValue;
}
