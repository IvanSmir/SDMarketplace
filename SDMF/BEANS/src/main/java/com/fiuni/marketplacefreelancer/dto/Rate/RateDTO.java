package com.fiuni.marketplacefreelancer.dto.Rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@XmlRootElement(name = "rate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("amount")
    @XmlElement
    private double amount;

    @JsonProperty("rateType")
    @XmlElement
    private String rateType;

    @JsonProperty("profileId")
    @XmlElement
    private String profileId;

    @Override
    public String toString() {
        return "RateDTO[" +
                "_amount=" + amount +
                ", _rateType='" + rateType + '\'' +
                ']';
    }
}
