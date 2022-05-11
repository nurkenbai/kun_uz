package com.company.entity;

import com.company.entity.BastEntity;
import com.company.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "email_history")
@Getter
@Setter
public class EmailEntity extends BastTimeAndIdEntity {
    @Column
    private String toEmail;
    @Column
    @Enumerated(EnumType.STRING)
    private EmailType type;
}