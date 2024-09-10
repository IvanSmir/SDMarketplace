package com.fiuni.marketplacefreelancer.dto.Role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement(name = "role")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    private String _name;

}
