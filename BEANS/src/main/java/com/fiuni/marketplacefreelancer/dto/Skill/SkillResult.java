package com.fiuni.marketplacefreelancer.dto.Skill;


import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "skillResult")

public class SkillResult extends BaseResult<SkillDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("skills")
    public List<SkillDTO> getSkills() {
        return getList();
    }

    public void setSkills(List<SkillDTO> skills) {
        super.setList(skills);
    }
}
