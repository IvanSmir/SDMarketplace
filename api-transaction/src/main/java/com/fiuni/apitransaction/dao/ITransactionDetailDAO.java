package com.fiuni.apitransaction.dao;


import com.fiuni.marketplacefreelancer.domain.transaction.TransactionDetailImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionDetailDAO extends JpaRepository<TransactionDetailImpl, Integer> {
    List<TransactionDetailImpl> findByTransactionId(int transactionId);  // Busca detalles por el ID de la transacción principal
}

