package com.fiuni.marketplacefreelancer.dto.Price;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;

import java.io.Serial;
import java.time.LocalTime;


@XmlRootElement(name = "price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PriceDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("price")
    @XmlElement
    private float price;

    @JsonProperty("projectId")
    @XmlElement
    private String projectId;

    @JsonProperty("currency")
    @XmlElement
    private String currency;

    @JsonProperty("date")
    @XmlElement
    private LocalTime date;

    @Override
    public String toString() {
        return "PriceDTO[" +
                "_price=" + price +
                ", _projectId='" + projectId + '\'' +
                ", _currency='" + currency + '\'' +
                ", _date=" + date +
                ']';
    }

}
