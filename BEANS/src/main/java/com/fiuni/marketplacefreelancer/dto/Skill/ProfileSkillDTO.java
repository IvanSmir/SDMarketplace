package com.fiuni.marketplacefreelancer.dto.Skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement(name = "profileSkill")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileSkillDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @JsonProperty("profileId")
    @XmlElement
    private String _profileId;

    @JsonProperty("skillId")
    @XmlElement
    private String _skillId;

}
