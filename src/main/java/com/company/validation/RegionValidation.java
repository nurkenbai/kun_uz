package com.company.validation;

import com.company.dto.RegionDTO;
import com.company.exception.AppBadRequestException;

public class RegionValidation {
    public static void toValidation(RegionDTO dto) {
        if (dto.getNameEn() == null || dto.getNameEn().trim().length() < 3) {
            throw new AppBadRequestException("Name_En not validate");
        }

        if (dto.getNameRu() == null || dto.getNameRu().trim().length() < 3) {
            throw new AppBadRequestException("Name_Ru not validate");
        }

        if (dto.getNameUz() == null || dto.getNameUz().trim().length() < 3) {
            throw new AppBadRequestException("Name_Uz not validate");
        }

        if (dto.getKey() == null || dto.getKey().trim().length() < 3) {
            throw new AppBadRequestException("key not validate");
        }
    }
}
