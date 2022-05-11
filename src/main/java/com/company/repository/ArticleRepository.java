package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTypeEntity;
import com.company.enums.ArticleStatus;
import com.company.mapper.ArticleSimpleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {



    Optional<ArticleEntity> findByTitle(String title);



    public List<ArticleEntity> findTop5ByTypeIdAndStatus(Integer typeId, ArticleStatus status, Sort sort);



    @Query(value = "SELECT a.id,a.title,a.description,a.attach_id,a.published_date " +
            " FROM article AS a  where  a.type_id =:typeId and status =:status order by created_date desc Limit 5", nativeQuery = true)
    public List<ArticleSimpleMapper> getTypeId(@Param("typeId") Integer typeId, @Param("status") String status);

    @Query(value = "SELECT a.id,a.title,a.description,a.attach_id,a.published_date " +
            " FROM article AS a  where  a.id =:articleId and status =:status order by created_date desc Limit 5", nativeQuery = true)
     Optional<ArticleSimpleMapper> getArticleId(@Param("articleId") Integer articleId, @Param("status") String status);



    public Page<ArticleEntity> findAllByTypeId(Integer typeId, Pageable pageable);

    Page<ArticleEntity> findByRegionId( Pageable pageable, Integer id);

    Page<ArticleEntity> findByCategoryId(Pageable pageable, Integer id);


    Optional<ArticleEntity> findByIdAndStatus(Integer id, ArticleStatus status);


}
