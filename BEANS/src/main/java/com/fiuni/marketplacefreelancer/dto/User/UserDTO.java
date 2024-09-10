package com.fiuni.marketplacefreelancer.dto.User;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "UserDTO[" +
                "_name='" + _name + '\'' +
                ", _email='" + _email + '\'' +
                ", _phone='" + _phone + '\'' +
                ", _createdAt='" + _createdAt + '\'' +
                ']';
    }

}
