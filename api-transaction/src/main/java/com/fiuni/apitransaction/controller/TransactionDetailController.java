package com.fiuni.apitransaction.controller;

import com.fiuni.apitransaction.service.transaction.TransactionDetailService;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-details")
public class TransactionDetailController {

    @Autowired
    private TransactionDetailService transactionDetailService;

    // Endpoint para agregar un detalle a una transacción existente
    @PostMapping("/add/{transactionId}")
    public TransactionDetailDTO addTransactionDetail(
            @PathVariable String transactionId,
            @RequestBody TransactionDetailDTO detailDTO) {
        return transactionDetailService.addTransactionDetail(transactionId, detailDTO);
    }

    // Endpoint para obtener los detalles de una transacción
    @GetMapping("/{transactionId}")
    public List<TransactionDetailDTO> getTransactionDetails(@PathVariable int transactionId) {
        return transactionDetailService.getTransactionDetails(transactionId);
    }
}
