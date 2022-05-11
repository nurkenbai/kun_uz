package com.company.controller;

import com.company.dto.RegionDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @PostMapping("/adm")
    public ResponseEntity<?> created(@RequestBody @Valid RegionDTO dto, HttpServletRequest request) {
        log.info("Region creat: {}",dto);
        Integer id = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.created(dto, id));
    }

    @GetMapping("/public")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/adm/{lang}")
    public ResponseEntity<?> get(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                 @RequestParam(value = "size",defaultValue = "5") Integer size,
                                 HttpServletRequest request,
                                 @PathVariable("lang") LangEnum lang) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAll(page, size, lang));
    }

    @GetMapping("/public/id")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(regionService.getById(id));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid RegionDTO dto,
                                    HttpServletRequest request) {
        log.info("Region update: {}",dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(dto, id));
    }

    @PutMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        log.info("Region delete: {}",id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.delete(id));
    }
}
