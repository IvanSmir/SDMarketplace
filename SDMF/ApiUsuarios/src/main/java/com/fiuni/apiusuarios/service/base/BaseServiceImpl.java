package com.fiuni.apiusuarios.service.base;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.dto.base.BaseDTO;
import com.fiuni.marketplacefreelancer.dto.base.BaseResult;

public abstract class BaseServiceImpl<DTO extends BaseDTO, DOMAIN extends IBaseDomain, R extends BaseResult<DTO>> implements IBaseService<DTO, R> {
    protected abstract DTO convertDomainToDto(DOMAIN domain);
    protected abstract DOMAIN convertDtoToDomain(DTO dto);
}
