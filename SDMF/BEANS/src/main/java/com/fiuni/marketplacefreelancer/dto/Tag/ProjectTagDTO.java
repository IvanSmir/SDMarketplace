package com.fiuni.marketplacefreelancer.dto.Tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@XmlRootElement(name = "projectTag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTagDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("projectId")
    @XmlElement
    private String _projectId;

    @JsonProperty("tagId")
    @XmlElement
    private String _tagId;

    @Override
    public String toString() {
        return "ProjectTagDTO[" +
                "_projectId='" + _projectId + '\'' +
                ", _tagId='" + _tagId + '\'' +
                ']';
    }
}
