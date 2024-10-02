package com.fiuni.marketplacefreelancer.service.price;

import com.fiuni.marketplacefreelancer.dao.price.IPriceDao;
import com.fiuni.marketplacefreelancer.dao.project.IProjectDao;
import com.fiuni.marketplacefreelancer.domain.price.PriceDomainImpl;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Price.PriceDTO;
import com.fiuni.marketplacefreelancer.dto.Price.PriceResult;
import com.fiuni.marketplacefreelancer.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService extends BaseServiceImpl<PriceDTO, PriceDomainImpl, PriceResult> {

    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IPriceDao priceDao;

    @Autowired
    private IProjectDao projectDao;

    @Autowired
    private CacheManager cacheManager;

    @Override
    protected PriceDTO converDomainToDto(PriceDomainImpl domain) {
        return modelMapper.map(domain, PriceDTO.class);
    }

    @Override
    protected PriceDomainImpl converDtoToDomain(PriceDTO dto) {
        return modelMapper.map(dto, PriceDomainImpl.class);
    }

    @Override
    public PriceDTO save(PriceDTO dto) {
        logger.info("Saving price with ID: {}", dto.getId());

        Optional<ProjectDomainImpl> project = projectDao.findById(dto.getProjectId());
        if (project.isEmpty()) {
            logger.error("Project not found");
            throw new AlreadyExistsException("Project not found");
        }

        Optional<PriceDomainImpl> price = priceDao.findByProjectIdAndActive(dto.getProjectId(), true);
        if (price.isPresent()) {
            price.get().setActive(false);
            logger.info("Deactivating current price for project ID: {}", dto.getProjectId());
            priceDao.save(price.get());
        }

        try {
            PriceDomainImpl priceDomain = converDtoToDomain(dto);
            PriceDomainImpl savedPriceDomain = priceDao.save(priceDomain);
            logger.info("Caching saved price with ID: {}", dto.getId());

            cacheManager.getCache("price").put(dto.getProjectId(), savedPriceDomain);

            return converDomainToDto(savedPriceDomain);
        } catch (Exception e) {
            logger.error("Error saving price", e);
            throw new RuntimeException("Error saving price", e);
        }
    }

    public PriceDTO getById(String id) {
        logger.info("Fetching price with ID: {}", id);

        PriceDTO cachedPrice = cacheManager.getCache("price").get(id, PriceDTO.class);
        if (cachedPrice != null) {
            return cachedPrice;
        }
        logger.info("Price not found in cache, querying database for ID: {}", id);
        return priceDao.findById(id)
                .map(price -> {
                    PriceDTO priceDTO = converDomainToDto(price);
                    logger.info("Caching fetched price with ID: {}", id);

                    cacheManager.getCache("price").put(id, priceDTO);
                    return priceDTO;
                })
                .orElseThrow(() -> new NotFoundException("Price", id));
    }

    public PriceResult getAll(Pageable pageable) {
        logger.info("Fetching all prices");
        Page<PriceDomainImpl> prices = priceDao.findAll(pageable);

        List<PriceDTO> priceDTOS = prices.getContent().stream()
                .map(this::converDomainToDto)
                .toList();

        priceDTOS.forEach(price -> {
            if (cacheManager.getCache("price").get(price.getId()) == null) {
                logger.info("Caching price with ID: {}", price.getId());
                cacheManager.getCache("price").put(price.getId(), price);
            } else {
                logger.info("Price with ID: {} is already in cache", price.getId());
            }
        });
        PriceResult priceResult = new PriceResult();
        priceResult.setPrice(priceDTOS);
        return priceResult;
    }

    public PriceDTO update(String id, PriceDTO dto) {
        Optional<ProjectDomainImpl> project = projectDao.findById(dto.getProjectId());
        if (project.isEmpty()) {
            throw new NotFoundException("Project", dto.getProjectId());
        }

        Optional<PriceDomainImpl> existingPrice = priceDao.findById(id);
        if (existingPrice.isEmpty()) {
            throw new NotFoundException("Price", id);
        }

        try {
            PriceDomainImpl priceDomain = converDtoToDomain(dto);
            PriceDomainImpl updatedPrice = priceDao.save(priceDomain);
            logger.info("Updating cache for price with ID: {}", id);

            cacheManager.getCache("price").put(id, updatedPrice);

            return converDomainToDto(updatedPrice);
        } catch (Exception e) {
            logger.error("Error updating price", e);
            throw new RuntimeException("Error updating price", e);
        }
    }

    public Boolean delete(String id) {
        logger.info("Deleting price with ID: {}", id);
        Optional<PriceDomainImpl> price = priceDao.findById(id);
        if (price.isPresent()) {
            priceDao.delete(price.get());
            logger.info("Removing price with ID: {} from cache", id);

            cacheManager.getCache("price").evict(id);

            return true;
        }
        logger.warn("Price with ID: {} not found", id);
        return false;
    }
}
