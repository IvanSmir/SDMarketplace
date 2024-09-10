package com.fiuni.marketplacefreelancer.dto.base;

import java.io.Serializable;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResult<DTO extends BaseDTO> implements Serializable {

    private static final long serialVersionUID = 1L;


    private List<DTO> _dtos;

    public Integer getTotal() {
        return null == _dtos ? 0 : _dtos.size();
    }
}
