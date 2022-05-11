package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/adm")
    public ResponseEntity<?> created(@RequestBody @Valid ProfileDTO dto, HttpServletRequest request) {
        log.info("Profile create: {}", dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.created(dto));
    }

    @GetMapping("/public")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(profileService.getAll());
    }

    @GetMapping("/adm")
    public ResponseEntity<?> get(@RequestBody Integer page,
                                 @RequestBody Integer size,
                                 HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll(page, size));
    }

    @GetMapping("/public/id")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        Integer id = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody @Valid ProfileDTO dto,
                                    HttpServletRequest request) {
        log.info("Profile update: {}", dto);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.update(dto, id));
    }

    @PutMapping("/public/image/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") String id,
                                         HttpServletRequest request) {
        Integer pid = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(profileService.uploadImage(pid, id));
    }

    @PutMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request) {
        log.info("Profile delete: {}", id);
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(id));
    }
}
