package com.company.service;


import com.company.dto.AttachDTO;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exception.AppBadRequestException;
import com.company.exception.AppForbiddenException;
import com.company.exception.EmailAlreadyExistsException;
import com.company.exception.PasswordOrEmailWrongException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AttachService attachService;

    public ProfileDTO login(AuthDTO dto) {
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        Optional<ProfileEntity> optional =
                profileRepository.findByEmailAndPassword(dto.getEmail(), pswd);
        if (optional.isEmpty()) {
            log.warn("Password or email wrong!");
            throw new PasswordOrEmailWrongException("Password or email wrong!");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            log.warn("No Access");
            throw new AppForbiddenException("No Access");
        }

        ProfileDTO profile = new ProfileDTO();

        profile.setEmail(entity.getEmail());
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setRole(entity.getRole());
        profile.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));

        // image
        AttachEntity image = entity.getImage();
        if (image != null) {
            AttachDTO imageDTO = new AttachDTO();
            imageDTO.setUrl(attachService.toOpenURL(image.getId()));
            profile.setAttach(imageDTO);
        }
        return profile;
    }

    public void registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email Already Exits");
            throw new EmailAlreadyExistsException("Email Already Exits");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());

        String pswd = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(pswd);

        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(entity);

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                sendVerificationEmail(entity);
//            }
//        };
//        thread.start();
    }



    public void verification(String jwt) {
        Integer userId = null;
        try {
            userId = JwtUtil.decodeAndGetId(jwt);
        } catch (JwtException e) {
            log.warn("Verification not completed");
            throw new AppBadRequestException("Verification not completed");
        }


        profileRepository.updateStatus(ProfileStatus.ACTIVE, userId);
    }

    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(entity.getId());
        builder.append("Salom Hello \n");
        builder.append("To verify your registration click to next link.");
        builder.append("http://localhost:8080/auth/verification/").append(jwt);
        builder.append("\nMazgi!");
        emailService.send(entity.getEmail(), "Activate Your Registration", builder.toString());

    }
}
