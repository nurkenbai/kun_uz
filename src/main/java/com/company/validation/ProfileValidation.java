package com.company.validation;

import com.company.dto.ProfileDTO;
import com.company.exception.AppBadRequestException;

public class ProfileValidation {
    public static void toValidation(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().trim().length() < 3) {
            throw new AppBadRequestException("name not fount");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().length() < 3) {
            throw new AppBadRequestException("surname not fount");
        }
        if (dto.getEmail() == null || dto.getName().trim().length() < 3 || dto.getEmail().contains("@")) {
            throw new AppBadRequestException("email not fount");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().length() < 5) {
            throw new AppBadRequestException("password not found");
        }

    }
}
