package com.fiuni.authapi.services;

import com.fiuni.authapi.dao.IRoleDao;
import com.fiuni.authapi.dao.IUserDao;

import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import com.fiuni.marketplacefreelancer.dto.User.UserDTO;
import com.fiuni.marketplacefreelancer.dto.User.UserLoginDTO;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;



@Service
@Slf4j
public class AuthenticationService {
    private final IUserDao userDao;
    private final IRoleDao roleDao;
    private final ModelMapper modelMapper;


    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            IUserDao userDao,
            AuthenticationManager authenticationManager,
            IRoleDao roleDao,
            ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.modelMapper = modelMapper;
    }


    protected UserDTO convertDomainToDto(UserDomainImpl domain) {
        return modelMapper.map(domain, UserDTO.class);
    }

    protected UserDomainImpl convertDtoToDomain(UserDTO dto) {
        return modelMapper.map(dto, UserDomainImpl.class);
    }



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
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            UserDomainImpl savedUser = userDao.save(user);  
            log.info("User saved successfully with ID: {}", savedUser.getId());
            UserDTO savedUserDTO = convertDomainToDto(savedUser);
            savedUserDTO.setRole_id(role.get().getId());
            return savedUserDTO;
        } catch (Exception e) {
            log.error("An unexpected error occurred while saving user: {}", dto.getEmail(), e);
            throw new RuntimeException("Error saving the user", e);
        }
    }

    public UserDomainImpl authenticate(UserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userDao.findByEmail(input.getEmail())
                .orElseThrow();
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
