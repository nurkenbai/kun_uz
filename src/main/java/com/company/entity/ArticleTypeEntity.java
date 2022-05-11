package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "article_type")
public class ArticleTypeEntity extends BastEntity{
}
