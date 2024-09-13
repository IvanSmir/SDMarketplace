package com.fiuni.marketplacefreelancer.dto.Skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@XmlRootElement(name = "profileSkill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSkillDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("profileId")
    @XmlElement
    private String profileId;

    @JsonProperty("skillId")
    @XmlElement
    private String skillId;

    @Override
    public String toString() {
        return "ProfileSkillDTO[" +
                "_profileId='" + profileId + '\'' +
                ", _skillId='" + skillId + '\'' +
                ']';
    }

}
