package com.fiuni.marketplacefreelancer.controller;

import com.fiuni.marketplacefreelancer.dto.Price.PriceDTO;
import com.fiuni.marketplacefreelancer.dto.Price.PriceResult;
import com.fiuni.marketplacefreelancer.service.price.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price")
@Slf4j
public class PriceController {
    @Autowired
    private PriceService priceService;

    @GetMapping
    public ResponseEntity<PriceResult> getAll(Pageable pageable) {
        return new ResponseEntity<>(priceService.getAll(pageable), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceDTO> getById(@PathVariable String id) {
        return new ResponseEntity<>(priceService.getById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody PriceDTO price) {
        return new ResponseEntity<>(priceService.save(price), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PriceDTO price) {
        return new ResponseEntity<>(priceService.update(id, price), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(priceService.delete(id), HttpStatus.NO_CONTENT);
    }
}
