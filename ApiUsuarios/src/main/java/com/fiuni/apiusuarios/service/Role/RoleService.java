package com.fiuni.apiusuarios.service.Role;

import com.fiuni.apiusuarios.dao.role.IRoleDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import com.fiuni.marketplacefreelancer.dto.Role.RoleResult;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleService extends BaseServiceImpl<RoleDTO, RoleDomainImpl, RoleResult> {

    private final IRoleDao roleDao;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    @Autowired
    public RoleService(IRoleDao roleDao, ModelMapper modelMapper, CacheManager cacheManager) {
        this.roleDao = roleDao;
        this.modelMapper = modelMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    protected RoleDTO convertDomainToDto(RoleDomainImpl domain) {
        return modelMapper.map(domain, RoleDTO.class);
    }

    @Override
    protected RoleDomainImpl convertDtoToDomain(RoleDTO dto) {
        return modelMapper.map(dto, RoleDomainImpl.class);
    }

    @Override
    @CachePut(value = "rolesCache", key = "'api_role_' + #result.id", condition = "#result != null")
    public RoleDTO save(RoleDTO dto) {
        validateRoleData(dto);
        log.info("Starting role save service for role name: {}", dto.getName());

        Optional<RoleDomainImpl> existingRole = roleDao.findByName(dto.getName());
        if (existingRole.isPresent()) {
            log.warn("Attempt to create a role that already exists: {}", dto.getName());
            throw new AlreadyExistsException("The role with name " + dto.getName() + " already exists");
        }

        try {
            RoleDomainImpl role = convertDtoToDomain(dto);
            RoleDomainImpl savedRole = roleDao.save(role);
            log.info("Role saved successfully with ID: {}", savedRole.getId());
            return convertDomainToDto(savedRole);

        } catch (Exception e) {
            log.error("An unexpected error occurred while saving role: {}", dto.getName(), e);
            throw new RuntimeException("Error saving the role", e);
        }
    }

    @Override
    @Cacheable(value = "rolesCache", key = "'api_role_' + #id")
    public RoleDTO getById(String id) {
        log.info("Starting role get by id service for role ID: {}", id);
        return roleDao.findById(id)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("Role", id));
    }

    @Cacheable(value = "rolesCache", key = "'api_role_name_' + #name")
    public RoleDTO getByName(String name) {
        log.info("Starting role get by name service for role name: {}", name);
        return roleDao.findByName(name)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("Role", name));
    }

    @Override
    public RoleResult getAll(Pageable pageable) {
        log.info("Starting role get all service");

        Page<String> roleIds = roleDao.findAllIds(pageable);
        List<RoleDTO> roleDTOs = new ArrayList<>();

        Cache cache = cacheManager.getCache("rolesCache");
        log.info("Cache found: {}", cache != null);
        for (String roleId : roleIds.getContent()) {
            log.info("Role ID: {}", roleId);
            RoleDTO cachedRole = cache != null ? cache.get("api_role_" + roleId, RoleDTO.class) : null;
            if (cachedRole != null) {
                log.info("Role {} found in cache", roleId);
                roleDTOs.add(cachedRole);
            } else {
                log.info("Role {} not found in cache", roleId);
                RoleDomainImpl role = roleDao.findById(roleId).orElse(null);
                if (role != null) {
                    RoleDTO roleDTO = convertDomainToDto(role);
                    if (cache != null) {
                        cache.put("api_role_" + roleId, roleDTO);
                        log.info("Role {} saved in cache", roleId);
                    }
                    roleDTOs.add(roleDTO);
                }
            }
        }

        RoleResult result = new RoleResult();
        result.setRoles(roleDTOs);

        return result;
    }


    @CacheEvict(value = "rolesCache", key = "'api_role_' + #id")
    public Boolean delete(String id) {
        log.info("Starting role delete service for role ID: {}", id);
        Optional<RoleDomainImpl> role = roleDao.findById(id);
        if (role.isPresent()) {
            roleDao.delete(role.get());
            log.info("Role deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Attempt to delete a role that does not exist: {}", id);
            return false;
        }
    }

    @CacheEvict(value = "rolesCache", key = "'api_role_' + #result.id", condition = "#result != null")
    @CachePut(value = "rolesCache", key = "'api_role_' + #result.id", condition = "#result != null")
    public RoleDTO update(String id, RoleDTO dto) {
        log.info("Starting update for role with id: {}", id);

        Optional<RoleDomainImpl> existingRoleOpt = roleDao.findById(id);
        if (existingRoleOpt.isEmpty()) {
            log.warn("Role with id {} not found", id);
            throw new InvalidDataException("Role with id " + id + " not found");
        }

        RoleDomainImpl existingRole = existingRoleOpt.get();

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            Optional<RoleDomainImpl> roleByName = roleDao.findByName(dto.getName());
            if (roleByName.isPresent() && !roleByName.get().getId().equals(id)) {
                log.warn("Attempt to update role with name {} which already exists", dto.getName());
                throw new AlreadyExistsException("Role with name " + dto.getName() + " already exists");
            }
            existingRole.setName(dto.getName());
        }

        try {
            RoleDomainImpl updatedRole = roleDao.save(existingRole);
            log.info("Role with id {} updated successfully", updatedRole.getId());
            return modelMapper.map(updatedRole, RoleDTO.class);
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating role with id: {}", id, e);
            throw new RuntimeException("Error updating the role", e);
        }
    }

    private void validateRoleData(RoleDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            log.error("Invalid role data: role name is null or empty");
            throw new InvalidDataException("Role name cannot be null or empty");
        }
    }
}
