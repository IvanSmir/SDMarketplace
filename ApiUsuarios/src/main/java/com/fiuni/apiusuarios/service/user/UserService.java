package com.fiuni.apiusuarios.service.user;

import com.fiuni.apiusuarios.dao.IRoleDao;
import com.fiuni.apiusuarios.dao.IUserDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.apiusuarios.utils.InvalidDataException;
import com.fiuni.apiusuarios.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import com.fiuni.marketplacefreelancer.dto.User.UserDTO;
import com.fiuni.marketplacefreelancer.dto.User.UserResult;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseServiceImpl<UserDTO, UserDomainImpl, UserResult> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleDao roleDao;


    @Override
    protected UserDTO converDomainToDto(UserDomainImpl domain) {
        return modelMapper.map(domain, UserDTO.class);
    }

    @Override
    protected UserDomainImpl converDtoToDomain(UserDTO dto) {
        return modelMapper.map(dto, UserDomainImpl.class);
    }

    @Override
    public UserDTO save(UserDTO dto) {

        logger.info("Starting user save service for user email: {}", dto.getEmail());
        validateUserData(dto);


        Optional<UserDomainImpl> existingUser = userDao.findByEmail(dto.getEmail());

        Optional<RoleDomainImpl> role = roleDao.findById(dto.getRole_id());

        if (existingUser.isPresent()) {
            logger.warn("Attempt to create a user that already exists: {}", dto.getEmail());
            throw new AlreadyExistsException("The user with email " + dto.getEmail() + " already exists");
        }
        if (role.isEmpty()) {
            logger.warn("Attempt to create a user with role that does not exist: {}", dto.getRole_id());
            throw new NotFoundException("Role", dto.getRole_id());
        }
        try {
            UserDomainImpl user = converDtoToDomain(dto);
            user.setRole(role.get());
            user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
            UserDomainImpl savedUser = userDao.save(user);
            logger.info("User saved successfully with ID: {}", savedUser.getId());
            return converDomainToDto(savedUser);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while saving user: {}", dto.getEmail(), e);
            throw new RuntimeException("Error saving the user", e);
        }
    }

    @Override
    public UserDTO getById(String id) {
        logger.info("Starting user get by id service for user ID: {}", id);
        return userDao.findById(id)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    @Override
    public UserResult getAll(Pageable pageable) {
        logger.info("Starting user get all service");
        Page<UserDomainImpl> users = userDao.findAll(pageable);

        List<UserDTO> userDTOs = users.getContent().stream()
                .map(this::converDomainToDto)
                .toList();
        UserResult userResult = new UserResult();
        userResult.setUsers(userDTOs);
        return userResult;
    }

    public Boolean delete(String id) {
        logger.info("Starting user delete service for user ID: {}", id);
        Optional<UserDomainImpl> user = userDao.findById(id);
        if (user.isPresent()) {
            userDao.delete(user.get());
            logger.info("User deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Attempt to delete a user that does not exist: {}", id);
            return false;
        }
    }

    public UserDTO update(String id, UserDTO dto) {
        logger.info("Starting update for user with id: {}", dto.getId());
        validateUserData(dto);

        Optional<UserDomainImpl> existingUserOpt = userDao.findById(dto.getId());
        Optional<RoleDomainImpl> role = roleDao.findById(dto.getRole_id());
        Optional<UserDomainImpl> userByEmail = userDao.findByEmail(dto.getEmail());
        if (existingUserOpt.isEmpty()) {
            logger.warn("User with id {} not found", dto.getId());
            throw new InvalidDataException("User with id " + dto.getId() + " not found");
        }
        if (role.isEmpty()) {
            logger.warn("Attempt to update a user with role that does not exist: {}", dto.getRole_id());
            throw new NotFoundException("Role", dto.getRole_id());
        }
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(dto.getId())) {
            logger.warn("Attempt to update user with email {} which already exists", dto.getEmail());
            throw new AlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }
        try {
            //only update the fields that are not null
            UserDomainImpl updatedUser = userDao.save(existingUserOpt.get());
            logger.info("User with id {} updated successfully", updatedUser.getId());
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating user with id: {}", dto.getId(), e);
            throw new RuntimeException("Error updating the user", e);
        }


    }


    public UserDTO getByEmail(String email) {
        logger.info("Starting user get by email service for user email: {}", email);
        return userDao.findByEmail(email)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("User", email));
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
