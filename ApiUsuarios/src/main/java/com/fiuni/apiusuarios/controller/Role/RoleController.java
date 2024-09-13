package com.fiuni.apiusuarios.controller.Role;

import com.fiuni.apiusuarios.service.Role.RoleService;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import com.fiuni.marketplacefreelancer.dto.Role.RoleResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1/role")
@Slf4j
public class RoleController{

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping
    public ResponseEntity<RoleResult> getAll(Pageable pageable) {
        return new ResponseEntity<>(roleService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable String id) {
        return new ResponseEntity<>(roleService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody RoleDTO role) {
        return new ResponseEntity<>(roleService.save(role), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody RoleDTO role) {
        return new ResponseEntity<>(roleService.update(id, role), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(roleService.delete(id), HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{name}")
    public ResponseEntity<RoleDTO> getByName(@PathVariable String name) {
        return new ResponseEntity<>(roleService.getByName(name), HttpStatus.OK);
    }

}
