package com.fiuni.marketplacefreelancer.dto.base;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResult<DTO extends BaseDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @XmlTransient
    private List<DTO> _dtos;

    protected List<DTO> getList() {
        return _dtos;
    }

    protected void setList(List<DTO> dtos) {
        _dtos = dtos;
    }

    public Integer getTotal() {
        return null == _dtos ? 0 : _dtos.size();
    }

}
