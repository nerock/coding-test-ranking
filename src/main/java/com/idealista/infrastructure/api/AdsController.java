package com.idealista.infrastructure.api;

import java.util.List;
import java.util.stream.Collectors;

import com.idealista.application.service.AdRatingService;
import com.idealista.application.service.AdService;
import com.idealista.infrastructure.api.mapper.Mapper;
import com.idealista.infrastructure.persistence.AdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ads")
public class AdsController {
    private AdRatingService adRatingService;
    private AdService adService;

    private Logger logger = LoggerFactory.getLogger(AdsController.class);

    public AdsController(AdRatingService adRatingService, AdService adService) {
        this.adRatingService = adRatingService;
        this.adService = adService;
    }

    @GetMapping("/quality")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        logger.info("Called /ads/quality GET endpoint to all ads as QualityAds");

        List<AdVO> ads = adService.getAll();

        List qualityAds = ads.stream()
                .map(ad -> Mapper.mapQualityAd(
                        ad,
                        adService.getAdPictureUrls(ad.getId())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(qualityAds, HttpStatus.OK);
    }

    @GetMapping("/irrelevant")
    public ResponseEntity<List<QualityAd>> irrelevantListing() {
        logger.info("Called /ads/irrelevant GET endpoint to all irrelevant ads as QualityAds");

        List<AdVO> ads = adService.getIrrelevantAds();

        List qualityAds = ads.stream()
                .map(ad -> Mapper.mapQualityAd(
                        ad,
                        adService.getAdPictureUrls(ad.getId())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(qualityAds, HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity<List<PublicAd>> publicListing() {
        logger.info("Called /ads/public GET endpoint to all relevant ads ordered by descending score as PublicAds");

        List<AdVO> ads = adService.getOrderedRelevantAds();

        List publicAds = ads.stream()
                .map(ad -> Mapper.mapPublicAd(
                        ad,
                        adService.getAdPictureUrls(ad.getId())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(publicAds, HttpStatus.OK);
    }

    @PostMapping("/rate")
    public ResponseEntity<Void> calculateScore() {
        logger.info("Called /ads/rate POST endpoint to rate all ads");

        adRatingService.rateAndSaveAds();

        return ResponseEntity.ok().build();
    }
}
