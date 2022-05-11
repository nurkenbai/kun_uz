package com.company.repository;

import com.company.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update CategoryEntity set nameEn = :nameEn,nameRu=:nameRn,nameUz=:nameUz,key=:key  where id = :id")
    Integer update(@Param("nameRn") String nameRu,
                   @Param("nameUz") String nameUz,
                   @Param("nameEn") String nameEn,
                   @Param("key") String key,
                   @Param("id") Integer id);
}
