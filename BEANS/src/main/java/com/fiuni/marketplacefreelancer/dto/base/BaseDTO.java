package com.fiuni.marketplacefreelancer.dto.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @XmlElement
    private Integer _id;

}
