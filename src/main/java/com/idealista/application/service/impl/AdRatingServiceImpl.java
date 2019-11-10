package com.idealista.application.service.impl;

import com.idealista.application.service.AdRatingService;
import com.idealista.application.service.rating.RatingRuleService;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdRatingServiceImpl implements AdRatingService {
    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;
    public static final int RELEVANT_SCORE = 40;

    private InMemoryPersistence inMemoryPersistence;
    private Set<RatingRuleService> ratingRuleServices;

    private final Logger logger = LoggerFactory.getLogger(AdRatingServiceImpl.class);

    public AdRatingServiceImpl(InMemoryPersistence inMemoryPersistence, Set<RatingRuleService> ratingRuleServices) {
        this.inMemoryPersistence = inMemoryPersistence;
        this.ratingRuleServices = ratingRuleServices;
    }

    @Override
    public void rateAndSaveAds() {
        logger.info("Rating and saving ads");

        List<AdVO> ads = inMemoryPersistence.findAllAds();

        List<AdVO> ratedAds = rateAds(ads);

        inMemoryPersistence.saveAds(ratedAds);
    }

    public List<AdVO> rateAds(List<AdVO> ads) {
        logger.info("Rating ads");

        return ads.stream()
                .map(this::rateAd)
                .collect(Collectors.toList());
    }

    public AdVO rateAd(AdVO ad) {
        logger.info("Rating ad: {}", ad);

        int score = calculateScore(ad);

        return setAdScore(ad, score);
    }

    private AdVO setAdScore(AdVO ad, int score) {
        logger.info("Setting score {} for ad: {}", score, ad);

        if (ad.isRelevant() && score < RELEVANT_SCORE) {
            ad.setIrrelevantSince(new Date());
        } else if (score >= RELEVANT_SCORE) {
            ad.setIrrelevantSince(null);
        }

        ad.setScore(score);

        return ad;
    }

    private int calculateScore(AdVO ad) {
        logger.info("Calculating score for Ad: {}", ad);

        int score = ratingRuleServices
                .stream()
                .mapToInt(ratingRuleService -> ratingRuleService.calculate(ad))
                .sum();

        return getScoreBetweenLimits(score);
    }

    private int getScoreBetweenLimits(int score) {
        logger.info("Getting score {} between {} and {} constraints", score, MIN_SCORE, MAX_SCORE);

        return Math.max(
                Math.min(score, MAX_SCORE),
                MIN_SCORE);
    }
}
