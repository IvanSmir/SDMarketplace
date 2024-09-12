package com.fiuni.apiusuarios.controller;

import com.fiuni.apiusuarios.service.user.UserService;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.apiusuarios.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.dto.User.UserDTO;
import com.fiuni.marketplacefreelancer.dto.User.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<UserResult> getAll(Pageable pageable) {
        try {
            UserResult userResult = userService.getAll(pageable);
            return new ResponseEntity<>(userResult, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            UserDTO user = userService.getById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception ex) {
            return new ResponseEntity<>("Error getting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDTO user) {
       try {
           UserDTO savedUser = userService.save(user);
           return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
       } catch (AlreadyExistsException ex) {
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
       }catch (Exception ex) {
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
