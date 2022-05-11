package com.company.service;

import com.company.dto.AttachDTO;
import com.company.entity.AttachEntity;
import com.company.exception.AppBadRequestException;
import com.company.exception.DownloadNotFoundException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.AttachRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.upload.folder}")
    private String attachFolder;
    @Value("${server.domain.name}")
    private String domainName;

    public AttachDTO upload(MultipartFile file) {
        String pathFolder = getYmDString();
        File folder = new File(attachFolder + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        AttachEntity entity = saveAttach(key, pathFolder, extension, file);
        AttachDTO dto = toDTO(entity);
        try {// uploads/2022/04/23/dasdasd-dasdasda-asdasda-asdasd.jpg
            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachFolder + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dto;


    }

    public byte[] open(String key) {
        byte[] data;
        try {
            AttachEntity entity = get(key);
            String path = entity.getPath() + "/" + key + "." + entity.getExtension();
            Path file = Paths.get(attachFolder + path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Boolean delete(String key) {
        AttachEntity entity = get(key);

        File file = new File(attachFolder + entity.getPath() +
                "/" + entity.getId() + "." + entity.getExtension());

        if (file.delete()) {
            attachRepository.deleteById(key);
            return true;
        } else{
            log.warn("Could not read the file!");
            throw new AppBadRequestException("Could not read the file!");
        }

    }
    public ResponseEntity<Resource> download(String key) {
        try {
            AttachEntity entity = get(key);
            String path = entity.getPath() + "/" + key + "." + entity.getExtension();
            Path file = Paths.get(attachFolder + path);
            System.out.println(file.toUri());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + entity.getOrigenName() + "\"")
                        .body(resource);

            } else {
                log.warn("Could not read the file!");
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            log.warn("Error: ",e.getMessage());
            throw new DownloadNotFoundException("Error: " + e.getMessage());
        }
    }

    public List<AttachDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<AttachDTO> dtoList = new ArrayList<>();
        attachRepository.findAll(pageable).stream().forEach(entity -> {
            dtoList.add(toDTO(entity));
        });

        return dtoList;
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            log.warn("Attach not found");
            throw new ItemNotFoundException("Attach not found");
        });
    }

    public AttachDTO getId(String id) {
        return toDTO(attachRepository.findById(id).orElseThrow(() -> {
            log.warn("Attach not found");
            throw new ItemNotFoundException("Attach not found");
        }));
    }

    public AttachEntity saveAttach(String key, String pathFolder, String extension, MultipartFile file) {
        AttachEntity entity = new AttachEntity();
        entity.setId(key);
        entity.setPath(pathFolder);
        entity.setOrigenName(file.getOriginalFilename());
        entity.setExtension(extension);
        entity.setSize(file.getSize());
        attachRepository.save(entity);
        return entity;
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setOrigenName(entity.getOrigenName());
        dto.setPath(entity.getPath());
        dto.setUrl(domainName + "/attach/download/" + entity.getId());
        return dto;
    }

    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    public String toOpenURL(String id) {
        return domainName + "/attach/open_general/" + id;
    }
    public AttachDTO toOpenURLDTO(String id) {
        return new AttachDTO(domainName + "/attach/open_general/" + id);
    }
}
