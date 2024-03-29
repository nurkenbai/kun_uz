package com.company.service;

import com.company.dto.LikeDTO;
import com.company.entity.LikeEntity;
import com.company.enums.ProfileRole;
import com.company.exception.AppForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.mapper.LikeSimpleMapper;
import com.company.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    @Lazy
    private ArticleService articleService;

    public LikeDTO create(LikeDTO dto, Integer pId) {

        Optional<LikeEntity> oldLikeOptional = likeRepository.findByArticleIdAndProfileId(dto.getArticleId(), pId);
        if (oldLikeOptional.isPresent()) {
            oldLikeOptional.get().setStatus(dto.getStatus());
            likeRepository.save(oldLikeOptional.get());
            return dto;
        }

        LikeEntity entity = new LikeEntity();
        entity.setStatus(dto.getStatus());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(pId);
        likeRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public boolean update(Integer likeId, LikeDTO dto, Integer pId) {
        LikeEntity entity = get(likeId);

        if (!entity.getProfileId().equals(pId)) {
            log.warn("Not Access");
            throw new AppForbiddenException("Not Access");
        }
        entity.setStatus(dto.getStatus());
        likeRepository.save(entity);
        return true;
    }

    public boolean delete(Integer likeId, Integer pId, ProfileRole role) {
        LikeEntity entity = get(likeId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)) {
            likeRepository.delete(entity);
            return true;
        }
        log.warn("Not Access");
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<LikeDTO> listByArticleId(Integer articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<LikeEntity> pageList = likeRepository.findAllByArticleId(articleId, pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent()) {
            likeDTOList.add(toDTO(entity));
        }
        return new PageImpl<LikeDTO>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public PageImpl<LikeDTO> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<LikeEntity> pageList = likeRepository.findAllByProfileId(profileId, pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent()) {
            likeDTOList.add(toDTO(entity));
        }
        return new PageImpl<LikeDTO>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public LikeDTO getByArticleIdCount(Integer id) {
        LikeSimpleMapper count = likeRepository.countArticleLikeDisLike(id);
        LikeDTO dto = new LikeDTO();
        dto.setLikeCount(count.getLike_count());
        dto.setDisLikeCount(count.getDislike_count());
        return dto;
    }

    public PageImpl<LikeDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<LikeEntity> pageList = likeRepository.findAll(pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent()) {
            likeDTOList.add(toDTO(entity));
        }
        return new PageImpl<LikeDTO>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public LikeEntity get(Integer likeId) {
        return likeRepository.findById(likeId).orElseThrow(() -> {
            log.warn("Like Not Found");
            throw new ItemNotFoundException("Like Not found");
        });
    }

    public LikeDTO getByArticleId(Integer articleId, Integer pId) {
        Optional<LikeEntity> optional = likeRepository
                .findByArticleIdAndProfileId(articleId, pId);
        if (optional.isPresent()) {
            return toDTO(optional.get());
        }
        return new LikeDTO();
    }

    public LikeDTO toDTO(LikeEntity entity) {
        LikeDTO dto = new LikeDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setProfileId(entity.getProfileId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }
}
