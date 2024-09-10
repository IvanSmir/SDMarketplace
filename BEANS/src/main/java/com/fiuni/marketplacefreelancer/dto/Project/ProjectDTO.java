package com.fiuni.marketplacefreelancer.dto.Project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseDTO {
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

    @Override
    public String toString() {
        return "ProjectDTO[" +
                "_title='" + _title + '\'' +
                ", _description='" + _description + '\'' +
                ", _url='" + _url + '\'' +
                ", _startDate='" + _startDate + '\'' +
                ", _endDate='" + _endDate + '\'' +
                ", _status='" + _status + '\'' +
                ']';
    }
}
