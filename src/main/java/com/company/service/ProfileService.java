package com.company.service;

import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import com.company.exception.EmailAlreadyExistsException;
import com.company.exception.IdNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.validation.ProfileValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO created(ProfileDTO dto) {
        ProfileValidation.toValidation(dto);
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email Already Exits");
            throw new EmailAlreadyExistsException("Email Already Exits");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCreateDate(LocalDateTime.now());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ProfileDTO getById(Integer id) {
        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() ->{
            log.warn("Id not Found");
            throw new IdNotFoundException("Id not Found");
        });
        return toDTO(entity);

    }

    public ProfileEntity get(Integer id) {
        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() ->{
            log.warn("Id not Found");
            throw new IdNotFoundException("Id not Found");
        });
        return entity;

    }

    public List<ProfileDTO> getAll() {
        List<ProfileDTO> dtoList = new LinkedList<>();
        profileRepository.findAll().stream().forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;

    }

    public List<ProfileDTO> getAll(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<ProfileDTO> dtoList = new LinkedList<>();
        profileRepository.findAll(pageable).stream().forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;

    }

    public String update(ProfileDTO dto, Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email Already Exits");
            throw new EmailAlreadyExistsException("Email Already Exits");
        }
        Integer ubd = profileRepository.update(dto.getSurname(), dto.getName(), dto.getEmail(), dto.getPassword(), id);
        if (ubd > 0) {
            return "Update";
        }
        return "not update";
    }

    public String delete(Integer id) {
        profileRepository.deleteById(id);
        return "delete";
    }

    public ProfileDTO uploadImage(Integer pId, String att_Id) {
        ProfileEntity entity = profileRepository.uploadImage(pId, att_Id)
                .orElseThrow(() -> {
                    log.warn("Id not Found");
                    throw new IdNotFoundException("Id not found");
                });
        return toDTO(entity);
    }


    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setSurname(entity.getSurname());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setCreateDate(entity.getCreateDate());
        if (entity.getImageId() != null) {
            dto.setImageId(entity.getImageId());
        }
        return dto;
    }
}
