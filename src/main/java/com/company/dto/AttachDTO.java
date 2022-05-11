package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttachDTO {
    private String id;
    private String path;
    private String extension;
    private String origenName;
    private Long size;
    private LocalDateTime createdDate;
    private String url;
    public AttachDTO() {

    }

    public AttachDTO(String url) {
        this.url = url;
    }
}
