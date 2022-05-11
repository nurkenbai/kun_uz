package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.CategoryService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @PostMapping("/adm")
    public ResponseEntity<?> created(@RequestBody @Valid CategoryDTO dto, HttpServletRequest request) {
        log.info("Category creat: {}",dto);
        Integer id = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.created(dto, id));
    }

    @GetMapping("/public")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/adm/{lang}")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 HttpServletRequest request,
                                 @PathVariable("lang") LangEnum lang) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getAll(page, size, lang));
    }

    @GetMapping("/public/id")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid CategoryDTO dto,
                                    HttpServletRequest request) {
        log.info("Category Update: {}",dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.update(dto, id));
    }

    @PutMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        log.info("Category delete: {}",id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
