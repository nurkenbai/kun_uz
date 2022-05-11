package com.company.dto;

import com.company.enums.ProfileStatus;
import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO extends BastTimeAndIdDTO {
    private Integer id;
    @NotBlank(message = "name not fount")
    private String name;
    @NotBlank(message = "surname not fount")
    private String surname;
    private String email;
    @NotBlank(message = "password not fount")
    private String password;
    private ProfileRole role;
    private ProfileStatus active;
    private String imageId;
    private String jwt;
    private AttachDTO attach;
}
