package com.fiuni.marketplacefreelancer.dto.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@XmlRootElement(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("userId")
    @XmlElement
    private String userId;

    @JsonProperty("name")
    @XmlElement
    private String name;

    @JsonProperty("experience")
    @XmlElement
    private String experience;

    @JsonProperty("portfolioUrl")
    @XmlElement
    private String portfolioUrl;

    @JsonProperty("rate")
    @XmlElement
    private float rate;

    @JsonProperty("description")
    @XmlElement
    private String description;

    @Override
    public String toString() {
        return "ProfileDTO[" +
                "_userId='" + userId + '\'' +
                ", _name='" + name + '\'' +
                ", _experience='" + experience + '\'' +
                ", _portfolioUrl='" + portfolioUrl + '\'' +
                ", _rate=" + rate +
                ", _description='" + description + '\'' +
                ']';
    }
}
