package com.fiuni.marketplacefreelancer.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.time.LocalTime;

@XmlRootElement(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("message")
    @XmlElement
    private String message;

    @JsonProperty("senderId")
    @XmlElement
    private String senderId;

    @JsonProperty("receptorId")
    @XmlElement
    private String receptorId;

    @JsonProperty("projectId")
    @XmlElement
    private String projectId;

    @JsonProperty("date")
    @XmlElement
    private LocalTime date;


    @Override
    public String toString() {
        return "MessageDTO[" +
                "_message='" + message + '\'' +
                ", _senderId='" + senderId + '\'' +
                ", _receptorId='" + receptorId + '\'' +
                ", _projectId='" + projectId + '\'' +
                ", _date=" + date +
                ']';
    }
}
