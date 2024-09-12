package com.fiuni.marketplacefreelancer.dto.Rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@XmlRootElement(name = "profileRate")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileRate extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("profileId")
    @XmlElement
    private String _profileId;

    @JsonProperty("rateId")
    @XmlElement
    private String _rateId;

}
