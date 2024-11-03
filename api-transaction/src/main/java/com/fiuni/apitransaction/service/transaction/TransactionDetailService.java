package com.fiuni.apitransaction.service.transaction;

import com.fiuni.apitransaction.dao.ITransactionDAO;
import com.fiuni.apitransaction.dao.ITransactionDetailDAO;
import com.fiuni.apitransaction.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.domain.transaction.TransactionDetailImpl;
import com.fiuni.marketplacefreelancer.domain.transaction.TransactionImpl;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDetailDTO;
import com.fiuni.marketplacefreelancer.dto.Transaction.TransactionDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j

public class TransactionDetailService extends BaseServiceImpl<TransactionDetailDTO, TransactionDetailImpl, TransactionDetailResult> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITransactionDAO transactionDAO;

    @Autowired
    private ITransactionDetailDAO transactionDetailDAO;

    @Override
    protected TransactionDetailDTO converDomainToDto(TransactionDetailImpl domain) {
        return new TransactionDetailDTO(
                domain.getId(),
                domain.getItemDescription(),
                domain.getQuantity(),
                domain.getPrice(),
                domain.getSubtotal()
        );
    }

    @Override
    protected TransactionDetailImpl converDtoToDomain(TransactionDetailDTO dto) {
        TransactionDetailImpl detail = new TransactionDetailImpl();
        detail.setId(dto.getDetailId());
        if(dto.getId() != null && !dto.getId().isEmpty()){
            TransactionImpl transaction = transactionDAO.findById(dto.getId()).orElse(null);
            if(transaction != null){
                detail.setTransaction(transaction);
            }
        }
        detail.setItemDescription(dto.getItemDescription());
        detail.setQuantity(dto.getQuantity());
        detail.setPrice(dto.getPrice());
        detail.setSubtotal(dto.getPrice() * dto.getQuantity());
        return detail;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionDetailDTO save(TransactionDetailDTO dto) {
        Optional<TransactionImpl> transactionOpt = transactionDAO.findById(dto.getId());
        if (transactionOpt.isEmpty()) {
            throw new RuntimeException("Transaction not found with ID: " + dto.getId());
        }

        TransactionDetailImpl detail = converDtoToDomain(dto);
        TransactionDetailImpl savedDetail = transactionDetailDAO.save(detail);
        return converDomainToDto(savedDetail);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionDetailDTO getById(String id) {
        return transactionDetailDAO.findById(Integer.parseInt(id))
                .map(this::converDomainToDto)
                .orElseThrow(() -> new RuntimeException("Transaction Detail not found"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionDetailResult getAll(Pageable pageable) {
        Page<TransactionDetailImpl> page = transactionDetailDAO.findAll(pageable);
        List<TransactionDetailDTO> dtos = page.map(this::converDomainToDto).toList();

        TransactionDetailResult result = new TransactionDetailResult();
        result.setTransactionDetails(dtos);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean delete(Integer id) {
        if (transactionDetailDAO.existsById(id)) {
            transactionDetailDAO.deleteById(id);
            return true;
        }
        return false;
    }

    public TransactionDetailDTO addTransactionDetail(String transactionId, TransactionDetailDTO dto) {
        Optional<TransactionImpl> transactionOpt = transactionDAO.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            throw new RuntimeException("Transaction not found with ID: " + transactionId);
        }
        dto.setId(transactionId);
        TransactionDetailImpl detail = converDtoToDomain(dto);
        TransactionDetailImpl savedDetail = transactionDetailDAO.save(detail);
        return converDomainToDto(savedDetail);
    }

    public List<TransactionDetailDTO> getTransactionDetails(int transactionId) {
        List<TransactionDetailImpl> details = transactionDetailDAO.findByTransactionId(transactionId);
        return details.stream().map(this::converDomainToDto).collect(Collectors.toList());
    }
}



