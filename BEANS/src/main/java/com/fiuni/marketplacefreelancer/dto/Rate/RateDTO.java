package com.fiuni.marketplacefreelancer.dto.Rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement(name = "rate")
@Data
@EqualsAndHashCode(callSuper = false)
public class RateDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @JsonProperty("amount")
    @XmlElement
    private float _amount;

    @JsonProperty("rateType")
    @XmlElement
    private String _rateType;

}
