package com.fiuni.marketplacefreelancer.dto.User;

import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;

@XmlRootElement(name = "userResult")

public class UserResult extends BaseResult<UserDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("users")
    public List<UserDTO> getUsers() {
        return getList();
    }

    public void setUsers(List<UserDTO> users) {
        super.setList(users);
    }
}
