package com.fiuni.apiusuarios.controller;

import com.fiuni.apiusuarios.service.Role.RoleService;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import com.fiuni.marketplacefreelancer.dto.Role.RoleResult;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1/role")
public class RoleController{

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;


    @GetMapping
    public ResponseEntity<RoleResult> getAll(Pageable pageable) {

        try {
            RoleResult roleResult = roleService.getAll(pageable);
            return new ResponseEntity<>(roleResult, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable String id) {
        try {
            RoleDTO role = roleService.getById(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RoleDTO role) {
        logger.info("save");
        try {
            RoleDTO savedRole = roleService.save(role);
            return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
        } catch (AlreadyExistsException ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody RoleDTO role) {
        logger.info("update");
        try {
            RoleDTO updatedRole = roleService.update(id, role);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        logger.info("delete");
        try {
            Boolean deleted = roleService.delete(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
