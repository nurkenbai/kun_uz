package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.enums.LangEnum;
import com.company.exception.IdNotFoundException;
import com.company.repository.CategoryRepository;
import com.company.validation.CategoryValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO created(CategoryDTO dto, Integer pid) {
        CategoryValidation.toValidation(dto);
        CategoryEntity entity = new CategoryEntity();
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setProfileId(pid);
        entity.setKey(dto.getKey());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CategoryDTO getById(Integer id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Id not Found");
            throw new IdNotFoundException("Id not Found");
        });
        return toDTO(entity);

    }
    public CategoryDTO getById(Integer id,LangEnum lang) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() ->{
            log.warn("Id not Found");
            throw new IdNotFoundException("Id not Found");
        });
        return toDTO(entity,lang);

    }

    public CategoryEntity get(Integer id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() ->{
            log.warn("Id not Found");
            throw new IdNotFoundException("Id not Found");
        });
        return entity;

    }

    public List<CategoryDTO> getAll() {
        List<CategoryDTO> dtoList = new LinkedList<>();
        categoryRepository.findAll().stream().forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;

    }

    public List<CategoryDTO> getAll(Integer page, Integer size, LangEnum lang) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<CategoryDTO> dtoList = new LinkedList<>();
        categoryRepository.findAll(pageable).stream().forEach(entity -> dtoList.add(toDTO(entity, lang)));
        return dtoList;

    }

    public String update(CategoryDTO dto, Integer id) {

        Integer ubd = categoryRepository.update(dto.getNameRu(), dto.getNameUz(), dto.getNameEn(), dto.getKey(), id);
        if (ubd > 0) {
            return "Update";
        }
        return "not update";
    }

    public String delete(Integer id) {
        categoryRepository.deleteById(id);
        return "delete";
    }

    private CategoryDTO toDTO(CategoryEntity entity, LangEnum lang) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        switch (lang) {
            case en -> dto.setName(entity.getNameEn());
            case ru -> dto.setName(entity.getNameRu());
            case uz -> dto.setName(entity.getNameUz());
        }
        dto.setKey(entity.getKey());
        dto.setPid(entity.getProfileId());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        dto.setKey(entity.getKey());
        dto.setPid(entity.getProfileId());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }
}
