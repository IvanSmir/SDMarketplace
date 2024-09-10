package com.fiuni.marketplacefreelancer.dto.Price;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;


@XmlRootElement(name = "price")
@Data
@EqualsAndHashCode(callSuper = false)
public class PriceDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @JsonProperty("price")
    @XmlElement
    private float _price;

    @JsonProperty("projectId")
    @XmlElement
    private String _projectId;

    @JsonProperty("currency")
    @XmlElement
    private String _currency;

    @JsonProperty("date")
    @XmlElement
    private LocalTime _date;

}
