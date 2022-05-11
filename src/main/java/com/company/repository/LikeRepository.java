package com.company.repository;

import com.company.entity.CommentEntity;
import com.company.entity.LikeEntity;
import com.company.mapper.LikeSimpleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    Page<LikeEntity> findAllByArticleId(Integer articleId, Pageable pageable);

    Page<LikeEntity> findAllByProfileId(Integer profile, Pageable pageable);

    Optional<LikeEntity> findByArticleIdAndProfileId(Integer articleId, Integer profileId);

    @Query(value = "select sum( case  when status = 'LIKE' THEN 1 else 0 END ) like_count," +
            "sum( case  when status = 'LIKE' THEN 0 else 1 END ) dislike_count" +
            "from like_table",nativeQuery = true)
    LikeSimpleMapper countArticleLikeDisLike(Integer articleId);

}
