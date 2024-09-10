package com.fiuni.marketplacefreelancer.dto.Tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @XmlElement
    private String _name;

    @JsonProperty("description")
    @XmlElement
    private String _description;

    @Override
    public String toString() {
        return "TagDTO[" +
                "_name='" + _name + '\'' +
                ", _description='" + _description + '\'' +
                ']';
    }
}
