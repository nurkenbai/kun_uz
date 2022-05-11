package com.company.entity;

import com.company.enums.ProfileStatus;
import com.company.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "profil")
public class ProfileEntity extends BastTimeAndIdEntity {

    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private String password;


    @Column
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name = "attach_id")
    private String imageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",insertable = false, updatable = false)
    private AttachEntity image;


}
