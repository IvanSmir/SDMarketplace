package com.fiuni.apitransaction.service.base;

import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;

public interface IBaseService <DTO extends BaseDTO, R extends BaseResult<DTO>> {
    public DTO save(DTO dto);

    public DTO getById(String id);

    public R getAll(Pageable pageable);

}
