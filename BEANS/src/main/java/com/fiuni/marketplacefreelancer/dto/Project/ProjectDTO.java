package com.fiuni.marketplacefreelancer.dto.Project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@XmlRootElement(name = "project")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("userId")
    @XmlElement
    private String _userId;

    @JsonProperty("title")
    @XmlElement
    private String _title;

    @JsonProperty("description")
    @XmlElement
    private String _description;

    @JsonProperty("url")
    @XmlElement
    private String _url;

    @JsonProperty("startDate")
    @XmlElement
    private String _startDate;

    @JsonProperty("endDate")
    @XmlElement
    private String _endDate;

    @JsonProperty("status")
    @XmlElement
    private String _status;

}
