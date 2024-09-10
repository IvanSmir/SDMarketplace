package com.fiuni.marketplacefreelancer.dto.Rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "rateResult")

public class RateResult extends BaseResult<RateDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("rates")
    public List<RateDTO> getRates() {
        return getList();
    }

    public void setRates(List<RateDTO> rates) {
        super.setList(rates);
    }


}
