package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid ArticleDTO dto, HttpServletRequest request) {
        log.info("Article Creat: {}", dto);
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, pId));
    }

    @GetMapping("/public/list")
    public ResponseEntity<?> listByLang(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(articleService.list(page, size));
    }

    @GetMapping("/public/region/{id}")
    public ResponseEntity<?> listByRegion(@PathVariable("id") Integer id,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(articleService.listRegionById(id, page, size));
    }

    @GetMapping("/public/category/{id}")
    public ResponseEntity<?> listByCategory(@PathVariable("id") Integer id,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(articleService.listCategoryById(id, page, size));
    }


    @GetMapping("/public/type/{id}")
    public ResponseEntity<?> getByType(@PathVariable("id") Integer typeId) {
        return ResponseEntity.ok(articleService.getArticleByType(typeId));
    }

    @GetMapping("/public/public/{id}")
    public ResponseEntity<?> getPublic(@PathVariable("id") Integer id,
                                       @RequestHeader(name = "Accepted-Language", defaultValue = "uz") LangEnum lang) {
        return ResponseEntity.ok(articleService.getPublic(id, lang));
    }


    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid ArticleDTO dto,
                                    HttpServletRequest request) {
        log.info("Article Update: {}", dto);
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.update(id, dto, pId));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        log.info("delete: {}", id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PutMapping("/adm/published/{id}")
    public ResponseEntity<?> published(@PathVariable("id") Integer id,
                                       HttpServletRequest request) {
        log.info("Published: {}", id);
        Integer pid = JwtUtil.getIdFromHeader(request, ProfileRole.SUPERMODERATOR);
        return ResponseEntity.ok(articleService.published(id, pid));
    }

    @PutMapping("/adm/blocked/{id}")
    public ResponseEntity<?> blocked(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        log.info("Blocked: {}", id);
        Integer pid = JwtUtil.getIdFromHeader(request, ProfileRole.SUPERMODERATOR);
        return ResponseEntity.ok(articleService.blocked(id, pid));
    }
}
