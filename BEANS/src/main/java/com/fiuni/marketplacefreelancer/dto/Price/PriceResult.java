package com.fiuni.marketplacefreelancer.dto.Price;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "priceResult")
public class PriceResult extends BaseResult<PriceDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("prices")
    public List<PriceDTO> getPrice() {
        return getList();
    }


    public void setPrice(List<PriceDTO> price) {
        super.setList(price);
    }

}
