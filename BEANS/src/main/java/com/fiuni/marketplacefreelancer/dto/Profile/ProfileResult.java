package com.fiuni.marketplacefreelancer.dto.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "profileResult")
public class ProfileResult extends BaseResult<ProfileDTO> {
    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("profiles")
    public List<ProfileDTO> getProfiles() {
        return getList();
    }

    public void setProfiles(List<ProfileDTO> profiles) {
        super.setList(profiles);
    }

}
