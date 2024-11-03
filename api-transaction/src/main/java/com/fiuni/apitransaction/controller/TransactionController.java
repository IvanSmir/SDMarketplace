package com.fiuni.apitransaction.controller;


import com.fiuni.apitransaction.service.transaction.TransactionService;
import com.fiuni.marketplacefreelancer.domain.enums.PaymentStatus;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import com.fiuni.marketplacefreelancer.dto.Role.RoleResult;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDTO;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionResult;
import jakarta.transaction.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @GetMapping
    public ResponseEntity<TransactionResult> getAll(Pageable pageable) {
        //transactionService.anotherMethod();
        //return new ResponseEntity<>(new TransactionResult(), HttpStatus.OK);
        return new ResponseEntity<>(transactionService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@PathVariable String id) {
        return new ResponseEntity<>(transactionService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody TransactionDTO transaction) {
        return new ResponseEntity<>(transactionService.save(transaction), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody TransactionDTO transaction) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(transactionService.delete(id), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/pay/{id}")
    public ResponseEntity<?> pay(@PathVariable String id) {
        return new ResponseEntity<>(transactionService.pay(id), HttpStatus.NO_CONTENT);
    }
/*
    @GetMapping("/{state}")
    public ResponseEntity<TransactionResult> getByName(@PathVariable String state, Pageable pageable) {
        return new ResponseEntity<>(transactionService.getByState(PaymentStatus.valueOf(state), pageable), HttpStatus.OK);
    }
*/
}
