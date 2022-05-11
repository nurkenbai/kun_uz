package com.company.dto;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDTO extends BastTimeAndIdDTO {
    private LikeStatus status;
    private Integer profileId;
    private Integer articleId;

    private Long likeCount;
    private Long disLikeCount;
}
