package com.fiuni.marketplacefreelancer.dto.Tag;

import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "tagResult")

public class TagResult extends BaseResult<TagDTO> {

    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("tags")
    public List<TagDTO> getTags() {
        return getList();
    }

    public void setTags(List<TagDTO> tags) {
        super.setList(tags);
    }

}
