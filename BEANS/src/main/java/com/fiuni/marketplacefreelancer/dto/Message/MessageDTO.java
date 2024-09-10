package com.fiuni.marketplacefreelancer.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@XmlRootElement(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO extends BaseDTO {

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


    @Override
    public String toString() {
        return "MessageDTO[" +
                "_message='" + _message + '\'' +
                ", _senderId='" + _senderId + '\'' +
                ", _receptorId='" + _receptorId + '\'' +
                ", _projectId='" + _projectId + '\'' +
                ", _date=" + _date +
                ']';
    }
}
