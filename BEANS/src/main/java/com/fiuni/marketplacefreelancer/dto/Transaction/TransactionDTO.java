package com.fiuni.marketplacefreelancer.dto.Transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@XmlRootElement(name = "transaction")
@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @JsonProperty("projectId")
    @XmlElement
    private String _projectId;

    @JsonProperty("clientId")
    @XmlElement
    private String _clientId;

    @JsonProperty("freelancerId")
    @XmlElement
    private String _freelancerId;

    @JsonProperty("amount")
    @XmlElement
    private float _amount;

    @JsonProperty("type")
    @XmlElement
    private String _type;

    @JsonProperty("date")
    @XmlElement
    private LocalTime _date;

    @JsonProperty("description")
    @XmlElement
    private String _description;

    @JsonProperty("status")
    @XmlElement
    private String _status;

}
