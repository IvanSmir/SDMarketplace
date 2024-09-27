package com.fiuni.marketplacefreelancer.dto.User;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserLoginDTO extends BaseDTO {

    @NotBlank(message = "The email is required")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;
}
