package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BastTimeAndIdDTO {
    private Integer id;
    private LocalDateTime createDate;
}
