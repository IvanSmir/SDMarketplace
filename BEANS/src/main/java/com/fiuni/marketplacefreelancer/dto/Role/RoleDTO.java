package com.fiuni.marketplacefreelancer.dto.Role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    private String _name;

    @Override
    public String toString() {
        return "RoleDTO[" +
                "_name='" + _name + '\'' +
                ']';
    }
}
