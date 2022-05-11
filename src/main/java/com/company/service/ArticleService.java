package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.exception.ItemAlreadyExistsException;
import com.company.exception.ItemNotFoundException;
import com.company.mapper.ArticleSimpleMapper;
import com.company.repository.ArticleRepository;
import com.company.validation.ArticleValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RegionService regionService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private TagService tagService;

    public ArticleDTO create(ArticleDTO dto, Integer pId) {
        ArticleValidation.isValid(dto);

        Optional<ArticleEntity> optional = articleRepository.findByTitle(dto.getTitle());
        if (optional.isPresent()) {
            log.warn("This Article already used!");
            throw new ItemAlreadyExistsException("This Article already used!");
        }

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setProfileId(pId);

        entity.setStatus(ArticleStatus.CREATED);

        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setTypeId(dto.getTypeId());
        entity.setTagList(dto.getTagIdList());

        articleRepository.save(entity);
        return toDTO(entity);
    }

    public List<ArticleDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        articleRepository.findAll(pageable).forEach(entity -> {
            dtoList.add(toDTO(entity));
        });

        return dtoList;
    }

    public List<ArticleDTO> listRegionById(Integer id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        articleRepository.findByRegionId(pageable, id).forEach(entity -> {
            dtoList.add(toDTO(entity));
        });

        return dtoList;
    }

    public List<ArticleDTO> listCategoryById(Integer id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        articleRepository.findByCategoryId( pageable, id).forEach(entity -> {
            dtoList.add(toDTO(entity));
        });

        return dtoList;
    }

    public ArticleDTO update(Integer id, ArticleDTO dto, Integer pId) {
        ProfileEntity profileEntity = profileService.get(pId);

        ArticleValidation.isValid(dto);

        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Not Found");
                    throw new ItemNotFoundException("Not Found!");}  );


        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        ;
        entity.setProfile(profileEntity);


        articleRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean delete(Integer id) {
        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() ->   {
                    log.warn("Not Found");
                    throw new ItemNotFoundException("Not Found!");});


        articleRepository.deleteById(id);
        return true;
    }

    public List<ArticleDTO> getArticleByType(Integer typeId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        List<ArticleSimpleMapper> entityList = articleRepository.getTypeId(typeId, ArticleStatus.PUBLISHED.name());


        List<ArticleDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setPublishedDate(entity.getPublished_date());

            dto.setImage(attachService.toOpenURLDTO(entity.getAttach_id()));
            dtoList.add(dto);
        });
        return dtoList;
    }

    public ArticleDTO getPublic(Integer id, LangEnum lang) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndStatus(id, ArticleStatus.PUBLISHED);
        return toFulDTO(optional.get(), lang);
    }

    public ArticleDTO getAdmin(Integer id, LangEnum lang) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        return toFulDTO(optional.get(), lang);
    }

    private ArticleDTO toFulDTO(ArticleEntity entity, LangEnum lang) {
        ArticleDTO dto = toDTO(entity);

        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());

        dto.setLike(likeService.getByArticleIdCount(entity.getId()));
        dto.setCategory(categoryService.getById(entity.getCategoryId(), lang));
        dto.setRegion(regionService.getById(entity.getRegionId(), lang));
        dto.setArticleType(articleTypeService.getById(entity.getTypeId(), lang));
        dto.setTagDTOList(tagService.get(entity.getTagList(), lang));
        return dto;
    }


    private ArticleDTO toSimpleDTO(ArticleSimpleMapper entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttach_id()));

        return dto;
    }


    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());

        dto.setProfileId(entity.getProfileId());

        dto.setImage(attachService.getId(entity.getAttachId()));
        dto.setCreatedDate(entity.getCreateDate());
        dto.setPublishedDate(entity.getPublishedDate());

        return dto;
    }

    public ArticleEntity get(Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> {
            log.warn("Article Not found");
            throw new ItemNotFoundException("Article Not found");
        });
    }

    public String published(Integer id, Integer pId) {

        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Not Found!");
                    throw new ItemNotFoundException("Not Found!");});

        entity.setStatus(ArticleStatus.PUBLISHED);

        articleRepository.save(entity);
        return "Success";
    }

    public String blocked(Integer id, Integer pId) {
        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Not Found");
                    throw  new ItemNotFoundException("Not Found!");});


        entity.setStatus(ArticleStatus.BLOCKED);
        articleRepository.save(entity);
        return "Success";
    }
}
