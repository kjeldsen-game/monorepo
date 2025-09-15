package com.kjeldsen.player.rest.mapper.common;

public class EnumMapper {
    public static <S extends Enum<S>, T extends Enum<T>> T mapEnum(S sourceEnum, Class<T> targetEnumClass) {
        if (sourceEnum == null) {
            return null;
        }
        return Enum.valueOf(targetEnumClass, sourceEnum.name());
    }
}
