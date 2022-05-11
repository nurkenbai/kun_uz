package com.company.dto;

import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;
    @NotBlank(message = "Title not valid")
    private String title;
    @NotBlank(message = "Description not valid")
    private String description;
    @NotBlank(message = "Content not valid")
    private String content;


    private Integer profileId;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private ArticleStatus status;
    private LocalDateTime publishedDate;

    private String attachId;
    @NotNull(message = "Category not valid")
    private Integer categoryId;
    @NotNull(message = "Region not valid")
    private Integer regionId;
    @NotNull(message = "Type not valid")
    private Integer typeId;

    private Integer viewCount;
    private Integer sharedCount;

    private List<Integer> tagIdList;
    private List<TagDTO> tagDTOList;

    private AttachDTO image;
    private CategoryDTO category;
    private LikeDTO like;
    private RegionDTO region;
    private ArticleTypeDTO articleType;
}
