package com.fiuni.marketplacefreelancer.dao.Transaction;

import com.fiuni.marketplacefreelancer.domain.transaction.TransactionImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionDao extends JpaRepository<TransactionImpl, Integer> {
}
