package com.fiuni.apiusuarios.controller.User;

import com.fiuni.apiusuarios.service.user.UserService;
import com.fiuni.marketplacefreelancer.dto.User.UserDTO;
import com.fiuni.marketplacefreelancer.dto.User.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<UserResult> getAll(Pageable pageable) {
        UserResult userResult = userService.getAll(pageable);
        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        UserDTO user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody UserDTO user) {
        UserDTO savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserDTO user) {
        UserDTO updatedUser = userService.update(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean deleted = userService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<UserResult> getByRoleId(@PathVariable String roleId, Pageable pageable) {
          UserResult userResult = userService.getByRoleId(roleId, pageable);
        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

}
