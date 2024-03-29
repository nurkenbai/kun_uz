package com.company.service;

import com.company.dto.CommentDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.enums.ProfileRole;
import com.company.exception.AppForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleService articleService;

    public CommentDTO create(CommentDTO dto, Integer pId) {
        ArticleEntity articleEntity = articleService.get(dto.getArticleId());

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(dto.getContent());
        commentEntity.setArticleId(dto.getArticleId());
        commentEntity.setProfileId(pId);
        commentRepository.save(commentEntity);

        dto.setId(commentEntity.getId());
        return dto;
    }

    public boolean update(Integer commentId, CommentDTO dto, Integer pId) {
        CommentEntity commentEntity = get(commentId);

        if (!commentEntity.getProfileId().equals(pId)) {
            log.warn("Not Access");
            throw new AppForbiddenException("Not Access");
        }
        commentEntity.setContent(dto.getContent());

        commentRepository.save(commentEntity);
        return true;
    }

    public boolean delete(Integer commentId, Integer pId, ProfileRole role) {
        CommentEntity commentEntity = get(commentId);
        if (commentEntity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)) {
            commentRepository.delete(commentEntity);
            return true;
        }
        log.warn("Not Access");
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<CommentDTO> listByArticleId(Integer articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<CommentEntity> pageList = commentRepository.findAllByArticleId(articleId, pageable);

        List<CommentDTO> commentDTOList = new LinkedList<>();
        for (CommentEntity commentEntity : pageList.getContent()) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(commentDTOList, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentDTO> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<CommentEntity> pageList = commentRepository.findAllByProfileId(profileId, pageable);

        List<CommentDTO> commentDTOList = new LinkedList<>();
        for (CommentEntity commentEntity : pageList.getContent()) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(commentDTOList, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<CommentEntity> pageList = commentRepository.findAll(pageable);

        List<CommentDTO> commentDTOList = new LinkedList<>();
        for (CommentEntity commentEntity : pageList.getContent()) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(commentDTOList, pageable, pageList.getTotalElements());
    }

    public CommentEntity get(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.warn("Comment Not found");
            throw new ItemNotFoundException("Comment Not found");
        });
    }

    public CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfileId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreateDate());

        return dto;
    }

}
