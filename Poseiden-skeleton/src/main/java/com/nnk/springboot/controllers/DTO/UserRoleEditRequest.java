package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEditRequest {
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "fullname is mandatory")
    private String fullname;

    @NotBlank(message = "role is mandatory")
    private String role;
}
