package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class AuthDTO {
    @NotBlank(message = "email required")
    @Email(message = "emali required")
    private String email;

    @NotBlank(message = "password required")
    @Size(min=4,max = 15,message = "About Me must between 10 and 200 characters")
    private String password;
}