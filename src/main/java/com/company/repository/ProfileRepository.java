package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    @Transactional
    @Modifying
    @Query("update ProfileEntity set surname = :surname,name=:name,email=:email,password=:pas  where id = :id")
    Integer update(@Param("surname") String surname, @Param("name") String name,
                   @Param("email") String email,
                   @Param("pas") String pas, @Param("id") Integer id);
    Optional<ProfileEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set image.id = :aId where id = :pid")
    Optional<ProfileEntity> uploadImage(@Param("pid")Integer pid,@Param("aId")String aId);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String pswd);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status = :status where id = :id")
    int updateStatus(@Param("status") ProfileStatus status, @Param("id") Integer id);
}
