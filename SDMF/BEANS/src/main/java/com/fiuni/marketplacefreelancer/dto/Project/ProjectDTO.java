package com.fiuni.marketplacefreelancer.dto.Project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@XmlRootElement(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("userId")
    @XmlElement
    private String userId;

    @JsonProperty("title")
    @XmlElement
    private String title;

    @JsonProperty("description")
    @XmlElement
    private String description;

    @JsonProperty("url")
    @XmlElement
    private String url;

    @JsonProperty("startDate")
    @XmlElement
    private String startDate;

    @JsonProperty("endDate")
    @XmlElement
    private String endDate;

    @JsonProperty("status")
    @XmlElement
    private String status;

    @Override
    public String toString() {
        return "ProjectDTO[" +
                "_title='" + title + '\'' +
                ", _description='" + description + '\'' +
                ", _url='" + url + '\'' +
                ", _startDate='" + startDate + '\'' +
                ", _endDate='" + endDate + '\'' +
                ", _status='" + status + '\'' +
                ']';
    }
}
