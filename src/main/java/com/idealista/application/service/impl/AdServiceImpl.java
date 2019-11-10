package com.idealista.application.service.impl;

import com.idealista.application.service.AdService;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {
    private InMemoryPersistence inMemoryPersistence;

    private final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    public AdServiceImpl(InMemoryPersistence inMemoryPersistence) { this.inMemoryPersistence = inMemoryPersistence; }

    @Override
    public List<AdVO> getOrderedRelevantAds() {
        logger.info("Retrieving relevant ads ordered by descending score");

        return inMemoryPersistence.findAllAdsIrrelevantSinceIsNullOrderByScoreDesc();
    }

    @Override
    public List<String> getAdPictureUrls(int adId) {
        logger.info("Retrieving picture urls for ad with id: {}", adId);

        return inMemoryPersistence.findAdPicturesUrlById(adId);
    }

    @Override
    public List<AdVO> getAll() {
        logger.info("Retrieving all ads");

        return inMemoryPersistence.findAllAds();
    }

    public List<AdVO> getIrrelevantAds() {
        logger.info("Retrieving irrelevant ads");

        return inMemoryPersistence.findIrrelevantAds();
    };
}
