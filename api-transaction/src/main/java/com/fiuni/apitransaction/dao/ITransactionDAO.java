package com.fiuni.apitransaction.dao;


import com.fiuni.marketplacefreelancer.domain.enums.PaymentStatus;
import com.fiuni.marketplacefreelancer.domain.transaction.TransactionImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ITransactionDAO extends JpaRepository<TransactionImpl, String> {
    Page<TransactionImpl> findByStatus(PaymentStatus paymentStatus, Pageable pageable);
}
