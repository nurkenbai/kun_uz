package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BastDTO extends BastTimeAndIdDTO {
    private Integer id;
    @NotBlank(message = "name_ru not validate")
    private String nameRu;
    @NotBlank(message = "name_en not validate")
    private String nameEn;
    @NotBlank(message = "name_uz not validate")
    private String nameUz;
    private String name;
    @NotBlank(message = "key not validate")
    private String key;
    private Integer pid;
}
