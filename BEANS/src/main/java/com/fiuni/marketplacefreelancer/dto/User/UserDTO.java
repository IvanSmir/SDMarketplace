package com.fiuni.marketplacefreelancer.dto.User;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@XmlRootElement(name = "user")
@Data
@EqualsAndHashCode(callSuper = false)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    @NotBlank(message = "The name is required")
    private String name;

    @JsonProperty("email")
    @XmlElement
    @NotBlank(message = "The email is required")
    private String email;

    @JsonProperty("phone")
    @XmlElement
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @XmlTransient
    @NotBlank(message = "The password is required")
    private String password;

    @JsonProperty("role_id")
    @XmlElement
    @NotBlank(message = "The role is required")
    private String role_id;

}
