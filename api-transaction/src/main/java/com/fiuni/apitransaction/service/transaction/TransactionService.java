package com.fiuni.apitransaction.service.transaction;


import com.fiuni.apitransaction.dao.IProjectDAO;
import com.fiuni.apitransaction.dao.ITransactionDAO;
import com.fiuni.apitransaction.dao.ITransactionDetailDAO;
import com.fiuni.apitransaction.dao.IUserDao;
import com.fiuni.apitransaction.service.base.BaseServiceImpl;
import com.fiuni.apitransaction.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.enums.PaymentStatus;
import com.fiuni.marketplacefreelancer.domain.enums.TransactionType;
import com.fiuni.marketplacefreelancer.domain.transaction.TransactionDetailImpl;
import com.fiuni.marketplacefreelancer.domain.transaction.TransactionImpl;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDTO;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDetailDTO;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionResult;
import jakarta.transaction.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class TransactionService extends BaseServiceImpl<TransactionDTO, TransactionImpl, TransactionResult> {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITransactionDAO transactionDAO;

    @Autowired
    private ITransactionDetailDAO transactionDetailDAO;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IProjectDAO projectDAO;

    @Override
    //@Transactional(propagation = Propagation.SUPPORTS)
    protected TransactionDTO converDomainToDto(TransactionImpl domain) {
        //log.info("Comienza la transaction, supports");
        System.out.println(domain.getId());
        TransactionDTO dto = new TransactionDTO();
        dto.set_transactionId(domain.getId());
        dto.setId(domain.getId() + "");
        dto.set_projectId(domain.getProject().getId());
        dto.set_clientId(domain.getClient().getId());
        dto.set_freelancerId(domain.getFreelancer().getId());
        dto.set_amount(domain.getAmount());
        dto.set_type(domain.getType().name());
        dto.set_date(LocalDateTime.now());
        dto.set_status(domain.getStatus().name());
        dto.set_description(domain.getDescription());
        dto.set_details(domain.getDetails().stream().map(this::convertDetailDomainToDTO).toList());

        return dto;
        //throw new RuntimeException();
    }

    @Override
    //@Transactional(propagation = Propagation.SUPPORTS)
    protected TransactionImpl converDtoToDomain(TransactionDTO dto) {
        log.info("Comienza la transaction, supports");
        TransactionImpl domain = new TransactionImpl();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            domain.setId(Integer.parseInt(dto.getId()));
        }
        domain.setType(TransactionType.valueOf(dto.get_type()));
        domain.setStatus(PaymentStatus.valueOf(dto.get_status()));
        domain.setDate(LocalDateTime.now());
        domain.setDescription(dto.get_description());
        domain.setAmount(dto.get_amount());
        domain.setDetails(dto.get_details().stream().map(this::convertDetailDtoToDomain).toList());
        System.out.println(dto.get_clientId());
        domain.setClient(userDao.getReferenceById(dto.get_clientId()));
        domain.setFreelancer(userDao.getReferenceById(dto.get_freelancerId()));
        domain.setProject(projectDAO.getReferenceById(dto.get_projectId()));
        return domain;
    }

    protected TransactionDetailDTO convertDetailDomainToDTO(TransactionDetailImpl domain){
        TransactionDetailDTO dto = new TransactionDetailDTO();
        dto.setId(domain.getTransaction().getId()+"");
        dto.setDetailId(domain.getId());
        dto.setPrice(domain.getPrice());
        dto.setQuantity(domain.getQuantity());
        dto.setSubtotal(domain.getSubtotal());
        dto.setItemDescription(domain.getItemDescription());
        return dto;
    }

    protected TransactionDetailImpl convertDetailDtoToDomain(TransactionDetailDTO dto) {
        TransactionDetailImpl domain = new TransactionDetailImpl();
        if(dto.getId() != null && !dto.getId().isEmpty()){
            TransactionImpl t = transactionDAO.findById(dto.getId()).orElse(null);
            if(t == null){
                throw new NotFoundException("No se encontro la transaccion con id ", dto.getId());
            }else{
                domain.setTransaction(t);
            }
        }
        domain.setId(dto.getDetailId());
        domain.setPrice(dto.getPrice());
        domain.setQuantity(dto.getQuantity());
        domain.setSubtotal(dto.getSubtotal());
        domain.setItemDescription(dto.getItemDescription());

        return domain;
    }

    @Override
    @CachePut(value = "transactions", key = "#dto._projectId")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionDTO save(TransactionDTO dto) {
        log.info("Comienza la transaction, requires-new");
        try{
            TransactionImpl returnValue = null;
            List<TransactionDetailDTO> details = dto.get_details();
            dto.set_details(Collections.emptyList());
            TransactionImpl t = transactionDAO.save(converDtoToDomain(dto));
            details.forEach(detail->{
                TransactionDetailImpl entity = convertDetailDtoToDomain(detail);
                entity.setTransaction(t);
                transactionDetailDAO.save(entity);
            });
            returnValue = transactionDAO.findById(t.getId()+"").orElse(null);
            if(returnValue == null) throw new NotFoundException("error", dto.getId());
            return converDomainToDto(returnValue);

        }catch(RuntimeException e) {
            log.error(e.getMessage());
            log.error("RollBack, por un error en el proceso");
            throw e;
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Cacheable(value = "transactions", key = "#id")
    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionDTO getById(String id) {
        log.info("Comienza la transaction, required");
        try {
            log.info("Starting user get by id service for transaction ID: {}", id);
            TransactionDTO returnValue = null;
            returnValue = converDomainToDto(transactionDAO.findById(id).get());
            //throw new RuntimeException();
            return returnValue;

        } catch (Exception e) {
            log.error("Se produjo un error al buscar", e);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult getAll(Pageable pageable) {
        try {
            log.info("getAll, transactional, required");
            //realizarOperacionLarga();

            Page<TransactionImpl> transactions = transactionDAO.findAll(pageable);

            List<TransactionDTO> transactionDTOS = transactions.getContent().stream()
                    .map(this::converDomainToDto)
                    .toList();

            TransactionResult transactionResult = new TransactionResult();
            transactionResult.setTransactions(transactionDTOS);
            //someMethod();
            //getById("81");
            log.info("transaction,exitosa");
            return transactionResult;
        } catch (Exception e) {
            log.error("error", e.getMessage());
            throw e;
        }
    }

    @CacheEvict(value = "transactions", key = "#id")
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean delete(String id) {
        log.info("Starting transaction delete service for transaction ID: {}", id);
        Optional<TransactionImpl> transaction = transactionDAO.findById(id);
        if(transaction.isPresent()){
            transactionDAO.delete(transaction.get());
            log.info("Transaction deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Attempt to delete a transaction that does not exist: {}", id);
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionDTO pay(String id) {
        log.info("Starting transaction, requires_new pay service for transaction ID: {}", id);
        Optional<TransactionImpl> transaction = transactionDAO.findById(id);
        if(transaction.isPresent()){
            log.info("Transaction paid successfully with ID: {}", id);
            TransactionImpl temp = transaction.get();
            temp.setStatus(PaymentStatus.COMPLETE);
            TransactionDTO dto = this.converDomainToDto(transactionDAO.save(temp));
            log.info("transaction exitosa");
            return dto;

        } else {
            log.warn("Attempt to pay a transaction that does not exist: {}", id);
            return null;
        }
    }

    public TransactionResult getByState(PaymentStatus paymentStatus, Pageable pageable){
        log.info("Starting transaction get by payment status: {}", paymentStatus);
        Page<TransactionImpl> transactions = transactionDAO.findByStatus(paymentStatus, pageable);

        List<TransactionDTO> transactionDTOS = transactions.getContent().stream()
                .map(this::converDomainToDto)
                .toList();

        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setTransactions(transactionDTOS);
        return transactionResult;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void someMethod() throws NotSupportedException {
        log.info("Comienza la transaction not_supported");
        log.info("Este es un mensaje de información");
        throw new NotSupportedException();
    }

    @Transactional(propagation = Propagation.NEVER)
    public void anotherMethod() {
        log.info("Comienza la transaction never");
        log.info("Este método no debe ser transaccional");
    }

    /*@Transactional(timeout = 1)
    public void realizarOperacionLarga() {
        log.info("Comienza la transaction para timeout");
        try {
            // Simulando operación larga
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupción durante la operación", e);
        } catch (Exception e) {
            throw e;
        }
    }*/
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateTransactionStatus(String id, PaymentStatus newStatus) {
        Optional<TransactionImpl> transaction = transactionDAO.findById(id);
        log.info("Comienza la transaction, mandatory");
        if (transaction.isPresent()) {
            TransactionImpl existingTransaction = transaction.get();
            existingTransaction.setStatus(newStatus);
            transactionDAO.save(existingTransaction);
            System.out.println("Transacción actualizada correctamente.");
        } else {
            System.out.println("Transacción no encontrada.");
        }
    }
}

