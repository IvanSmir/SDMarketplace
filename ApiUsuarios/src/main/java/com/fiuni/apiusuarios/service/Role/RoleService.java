package com.fiuni.apiusuarios.service.Role;

import com.fiuni.apiusuarios.dao.IRoleDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.apiusuarios.utils.InvalidDataException;
import com.fiuni.apiusuarios.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import com.fiuni.marketplacefreelancer.dto.Role.RoleResult;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService extends BaseServiceImpl<RoleDTO, RoleDomainImpl, RoleResult> {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    protected RoleDTO converDomainToDto(RoleDomainImpl domain) {
        return modelMapper.map(domain, RoleDTO.class);
    }

    @Override
    protected RoleDomainImpl converDtoToDomain(RoleDTO dto) {
        return modelMapper.map(dto, RoleDomainImpl.class);
    }

    @Override
    public RoleDTO save(RoleDTO dto) {

        validateRoleData(dto);

        logger.info("Starting role save service for role name: {}", dto.getName());

        Optional<RoleDomainImpl> existingRole = Optional.ofNullable(roleDao.findByName(dto.getName()));
        if (existingRole.isPresent()) {
            logger.warn("Attempt to create a role that already exists: {}", dto.getName());
            throw new AlreadyExistsException("The role with name " + dto.getName() + " already exists");
        }

        try {
            RoleDomainImpl role = converDtoToDomain(dto);

            RoleDomainImpl savedRole = roleDao.save(role);
            logger.info("Role saved successfully with ID: {}", savedRole.getId());

            return converDomainToDto(savedRole);

        } catch (Exception e) {
            logger.error("An unexpected error occurred while saving role: {}", dto.getName(), e);
            throw new RuntimeException("Error saving the role", e);
        }
    }

    @Override
    public RoleDTO getById(String id) {
        logger.info("Starting role get by id service for role ID: {}", id);
        return roleDao.findById(id)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("Role", id));
    }

    @Override
    public RoleResult getAll(Pageable pageable) {
        logger.info("Starting role get all service");
        Page<RoleDomainImpl> roles = roleDao.findAll(pageable);
        List<RoleDTO> rolesDTO = roles.getContent().stream()
                .map(this::converDomainToDto)
                .toList();
        RoleResult result = new RoleResult();
        result.setRoles(rolesDTO);
        return result;
    }

    public Boolean delete(String id) {
        logger.info("Starting role delete service for role ID: {}", id);
        Optional<RoleDomainImpl> role = roleDao.findById(id);
        if (role.isPresent()) {
            roleDao.delete(role.get());
            logger.info("Role deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Attempt to delete a role that does not exist: {}", id);
            return false;
        }
    }

    public RoleDTO update(String id, RoleDTO dto) {
        logger.info("Starting update for role with id: {}", id);

        Optional<RoleDomainImpl> existingRoleOpt = roleDao.findById(id);
        if (existingRoleOpt.isEmpty()) {
            logger.warn("Role with id {} not found", id);
            throw new InvalidDataException("Role with id " + id + " not found");
        }

        RoleDomainImpl existingRole = existingRoleOpt.get();

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            Optional<RoleDomainImpl> roleByName = Optional.ofNullable(roleDao.findByName(dto.getName()));
            if (roleByName.isPresent() && !roleByName.get().getId().equals(id)) {
                logger.warn("Attempt to update role with name {} which already exists", dto.getName());
                throw new AlreadyExistsException("Role with name " + dto.getName() + " already exists");
            }
            existingRole.setName(dto.getName());
        }

        try {
            RoleDomainImpl updatedRole = roleDao.save(existingRole);
            logger.info("Role with id {} updated successfully", updatedRole.getId());

            return modelMapper.map(updatedRole, RoleDTO.class);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating role with id: {}", id, e);
            throw new RuntimeException("Error updating the role", e);
        }
    }

    private void validateRoleData(RoleDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            logger.error("Invalid role data: role name is null or empty");
            throw new InvalidDataException("Role name cannot be null or empty");
        }
    }

}
