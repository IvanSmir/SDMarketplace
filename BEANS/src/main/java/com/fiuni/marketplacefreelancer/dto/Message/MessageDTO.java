package com.fiuni.marketplacefreelancer.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalTime;

@XmlRootElement(name = "message")
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("message")
    @XmlElement
    private String _message;

    @JsonProperty("senderId")
    @XmlElement
    private String _senderId;

    @JsonProperty("receptorId")
    @XmlElement
    private String _receptorId;

    @JsonProperty("projectId")
    @XmlElement
    private String _projectId;

    @JsonProperty("date")
    @XmlElement
    private LocalTime _date;

}
