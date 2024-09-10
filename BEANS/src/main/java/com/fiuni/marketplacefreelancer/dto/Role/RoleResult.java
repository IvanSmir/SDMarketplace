package com.fiuni.marketplacefreelancer.dto.Role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "roleResult")
public class RoleResult extends BaseResult<RoleDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("roles")
    public List<RoleDTO> getRoles() {
        return getList();
    }

    public void setRoles(List<RoleDTO> roles) {
        super.setList(roles);
    }
}
