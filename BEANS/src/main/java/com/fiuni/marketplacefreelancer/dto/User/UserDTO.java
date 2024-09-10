package com.fiuni.marketplacefreelancer.dto.User;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement(name = "user")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    private String _name;

    @JsonProperty("email")
    @XmlElement
    private String _email;



    @JsonProperty("phone")
    @XmlElement
    private String _phone;

    @JsonProperty("createdAt")
    @XmlElement
    private String _createdAt;

}
