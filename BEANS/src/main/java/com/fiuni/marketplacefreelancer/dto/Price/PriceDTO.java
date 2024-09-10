package com.fiuni.marketplacefreelancer.dto.Price;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;

import java.time.LocalTime;


@XmlRootElement(name = "price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

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

    @Override
    public String toString() {
        return "PriceDTO[" +
                "_price=" + _price +
                ", _projectId='" + _projectId + '\'' +
                ", _currency='" + _currency + '\'' +
                ", _date=" + _date +
                ']';
    }

}
