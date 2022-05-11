package com.company.controller;

import com.company.dto.TagDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.TagService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;


    @PostMapping("/adm")
    public ResponseEntity<?> created(@RequestBody @Valid TagDTO dto, HttpServletRequest request) {
        log.info("Tag creat: {}", dto);
        Integer id = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.created(dto, id));
    }

    @GetMapping("/public")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/adm/{lang}")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 HttpServletRequest request,
                                 @PathVariable("lang") LangEnum lang) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.getAll(page, size, lang));
    }

    @GetMapping("/public/id")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(tagService.getById(id));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid TagDTO dto,
                                    HttpServletRequest request) {
        log.info("Tag update: {}", dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.update(dto, id));
    }

    @PutMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        log.info("Tag delete: {}", id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.delete(id));
    }
}
