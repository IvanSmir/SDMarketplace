package com.fiuni.marketplacefreelancer.dto.Rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "profileRate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRate extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @JsonProperty("profileId")
    @XmlElement
    private String _profileId;

    @JsonProperty("rateId")
    @XmlElement
    private String _rateId;

    @Override
    public String toString() {
        return "ProfileRate[" +
                "_profileId='" + _profileId + '\'' +
                ", _rateId='" + _rateId + '\'' +
                ']';
    }
}
