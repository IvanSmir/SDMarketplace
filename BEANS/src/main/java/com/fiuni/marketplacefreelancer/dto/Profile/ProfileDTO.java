package com.fiuni.marketplacefreelancer.dto.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@XmlRootElement(name = "profile")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("userId")
    @XmlElement
    private String _userId;

    @JsonProperty("name")
    @XmlElement
    private String _name;

    @JsonProperty("experience")
    @XmlElement
    private String _experience;

    @JsonProperty("portfolioUrl")
    @XmlElement
    private String _portfolioUrl;

    @JsonProperty("rate")
    @XmlElement
    private float _rate;

    @JsonProperty("description")
    @XmlElement
    private String _description;

    @Override
    public String toString() {
        return "ProfileDTO[" +
                "_userId='" + _userId + '\'' +
                ", _name='" + _name + '\'' +
                ", _experience='" + _experience + '\'' +
                ", _portfolioUrl='" + _portfolioUrl + '\'' +
                ", _rate=" + _rate +
                ", _description='" + _description + '\'' +
                ']';
    }
}
