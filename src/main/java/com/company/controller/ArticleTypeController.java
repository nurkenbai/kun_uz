package com.company.controller;

import com.company.dto.ArticleTypeDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleTypeService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;


    @PostMapping("/adm")
    public ResponseEntity<?> created(@RequestBody @Valid ArticleTypeDTO dto, HttpServletRequest request) {
        log.info("Article Type Creat: {}",dto);
        Integer id = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.created(dto, id));
    }

    @GetMapping("/public")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(articleTypeService.getAll());
    }

    @GetMapping("/adm/{lang}")
    public ResponseEntity<?> get(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                 @RequestParam(value = "size",defaultValue = "5") Integer size,
                                 HttpServletRequest request,
                                 @PathVariable("lang") LangEnum lang) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getAll(page, size, lang));
    }

    @GetMapping("/public/id")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(articleTypeService.getById(id));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid ArticleTypeDTO dto,
                                    HttpServletRequest request) {
        log.info("Article Type Update: {}",dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(dto, id));
    }

    @PutMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        log.info("Delete: {}",id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.delete(id));
    }
}
