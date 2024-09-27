package com.fiuni.apiusuarios.service.user;

import com.fiuni.apiusuarios.dao.role.IRoleDao;
import com.fiuni.apiusuarios.dao.user.IUserDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import com.fiuni.marketplacefreelancer.dto.User.UserDTO;
import com.fiuni.marketplacefreelancer.dto.User.UserResult;
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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService extends BaseServiceImpl<UserDTO, UserDomainImpl, UserResult> {



    private final IUserDao userDao;
    private final IRoleDao roleDao;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    @Autowired
    public UserService(IUserDao userDao, IRoleDao roleDao, ModelMapper modelMapper, CacheManager cacheManager) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.modelMapper = modelMapper;
        this.cacheManager = cacheManager;
    }


    @Override
    protected UserDTO convertDomainToDto(UserDomainImpl domain) {
        return modelMapper.map(domain, UserDTO.class);
    }

    @Override
    protected UserDomainImpl convertDtoToDomain(UserDTO dto) {
        return modelMapper.map(dto, UserDomainImpl.class);
    }

    @Override
    @CachePut(value = "usersCache", key = "'api_user_' + #result.id", condition = "#result != null")
    public UserDTO save(UserDTO dto) {

        log.info("Starting user save service for user email: {}", dto.getEmail());
        validateUserData(dto);


        Optional<UserDomainImpl> existingUser = userDao.findByEmail(dto.getEmail());

        Optional<RoleDomainImpl> role = roleDao.findById(dto.getRole_id());

        if (existingUser.isPresent()) {
            log.warn("Attempt to create a user that already exists: {}", dto.getEmail());
            throw new AlreadyExistsException("The user with email " + dto.getEmail() + " already exists");
        }
        if (role.isEmpty()) {
            log.warn("Attempt to create a user with role that does not exist: {}", dto.getRole_id());
            throw new NotFoundException("Role", dto.getRole_id());
        }
        try {
            UserDomainImpl user = convertDtoToDomain(dto);
            user.setRole(role.get());
            user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
            UserDomainImpl savedUser = userDao.save(user);
            log.info("User saved successfully with ID: {}", savedUser.getId());
            return convertDomainToDto(savedUser);
        } catch (Exception e) {
            log.error("An unexpected error occurred while saving user: {}", dto.getEmail(), e);
            throw new RuntimeException("Error saving the user", e);
        }
    }

    @Override
    @Cacheable(value = "usersCache", key = "'api_user_' + #id")
    public UserDTO getById(String id) {
        log.info("Starting user get by id service for user ID: {}", id);
        return userDao.findById(id)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    @Override
    public UserResult getAll(Pageable pageable) {
        log.info("Starting user get all service");

        Page<String> userIds = userDao.findAllIds(pageable);
        List<UserDTO> userDTOs = new ArrayList<>();

        Cache cache = cacheManager.getCache("userCache");

        for (String userId : userIds.getContent()) {

            UserDTO cachedUser = cache != null ? cache.get("api_user_" + userId, UserDTO.class) : null;
            if (cachedUser != null) {
                log.info("User {} found in cache", userId);
                userDTOs.add(cachedUser);
            } else {
                log.info("User {} not found in cache", userId);
                UserDomainImpl user = userDao.findById(userId).orElse(null);
                if (user != null) {
                    UserDTO userDTO = convertDomainToDto(user);
                    if (cache != null) {
                        cache.put("api_user_" + userId, userDTO);
                        log.info("User {} saved in cache", userId);
                    }
                    userDTOs.add(userDTO);
                }
            }
        }

        UserResult result = new UserResult();
        result.setUsers(userDTOs);

        return result;
    }


    @CacheEvict(value = "userCache", key = "'api_user_' + #id")
    public Boolean delete(String id) {
        log.info("Starting user delete service for user ID: {}", id);
        Optional<UserDomainImpl> user = userDao.findById(id);
        if (user.isPresent()) {
            userDao.delete(user.get());
            log.info("User deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Attempt to delete a user that does not exist: {}", id);
            return false;
        }
    }

    @CacheEvict(value = "userCache", key = "'api_user_' + #result.id", condition = "#result != null")
    @CachePut(value = "userCache", key = "'api_user_' + #result.id", condition = "#result != null")
    public UserDTO update(String id, UserDTO dto) {
        log.info("Starting update for user with id: {}", dto.getId());
        validateUserData(dto);

        Optional<UserDomainImpl> existingUserOpt = userDao.findById(dto.getId());
        Optional<RoleDomainImpl> role = roleDao.findById(dto.getRole_id());
        Optional<UserDomainImpl> userByEmail = userDao.findByEmail(dto.getEmail());
        if (existingUserOpt.isEmpty()) {
            log.warn("User with id {} not found", dto.getId());
            throw new InvalidDataException("User with id " + dto.getId() + " not found");
        }
        if (role.isEmpty()) {
            log.warn("Attempt to update a user with role that does not exist: {}", dto.getRole_id());
            throw new NotFoundException("Role", dto.getRole_id());
        }
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(dto.getId())) {
            log.warn("Attempt to update user with email {} which already exists", dto.getEmail());
            throw new AlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }
        try {
            UserDomainImpl updatedUser = userDao.save(existingUserOpt.get());
            log.info("User with id {} updated successfully", updatedUser.getId());
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating user with id: {}", dto.getId(), e);
            throw new RuntimeException("Error updating the user", e);
        }


    }

    @Cacheable(value = "userCache", key = "'api_user_' + #email")
    public UserDTO getByEmail(String email) {
        log.info("Starting user get by email service for user email: {}", email);
        return userDao.findByEmail(email)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("User", email));
    }



    public UserResult getByRoleId(String roleId, Pageable pageable) {
        log.info("Starting user get by role id service for user role id: {}", roleId);

        Page<String> userIds = userDao.findAllIdsByRoleId(roleId, pageable);
        List<UserDTO> userDTOs = new ArrayList<>();

        Cache cache = cacheManager.getCache("userCache");

        for (String userId : userIds.getContent()) {
            UserDTO cachedUser = cache != null ? cache.get("api_user_" + userId, UserDTO.class) : null;
            if (cachedUser != null) {
                log.info("User {} found in cache", userId);
                userDTOs.add(cachedUser);
            } else {
                log.info("User {} not found in cache", userId);
                UserDomainImpl user = userDao.findById(userId).orElse(null);
                if (user != null) {
                    UserDTO userDTO = convertDomainToDto(user);
                    if (cache != null) {
                        cache.put("api_user_" + userId, userDTO);
                        log.info("User {} saved in cache", userId);
                    }
                    userDTOs.add(userDTO);
                }
            }
        }

        UserResult result = new UserResult();
        result.setUsers(userDTOs);

        return result;
    }


    private void validateUserData(UserDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new InvalidDataException("Invalid user data: user email is null or empty");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new InvalidDataException("Invalid user data: user password is null or empty");
        }
        if (dto.getRole_id() == null || dto.getRole_id().trim().isEmpty()) {
            throw new InvalidDataException("Invalid user data: user role is null or empty");
        }

    }


}
