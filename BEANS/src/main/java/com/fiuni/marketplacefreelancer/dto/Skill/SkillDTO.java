package com.fiuni.marketplacefreelancer.dto.Skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@XmlRootElement(name = "skill")
@Data
@EqualsAndHashCode(callSuper = false)
public class SkillDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    private String _name;

    @JsonProperty("description")
    @XmlElement
    private String _description;

}
