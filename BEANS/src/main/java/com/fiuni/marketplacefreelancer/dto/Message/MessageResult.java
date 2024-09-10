package com.fiuni.marketplacefreelancer.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "messageResult")
public class MessageResult extends BaseResult<MessageDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("messages")
    public List<MessageDTO> getMessages() {
        return getList();
    }

    public void setMessages(List<MessageDTO> messages) {
        super.setList(messages);
    }

}
