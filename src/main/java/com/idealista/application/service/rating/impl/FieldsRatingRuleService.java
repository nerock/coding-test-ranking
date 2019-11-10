package com.idealista.application.service.rating.impl;

import com.idealista.application.service.impl.AdRatingServiceImpl;
import com.idealista.application.service.rating.RatingRuleService;
import com.idealista.infrastructure.persistence.AdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FieldsRatingRuleService implements RatingRuleService {
    public static final int COMPLETE_FIELDS = 40;

    private final Logger logger = LoggerFactory.getLogger(FieldsRatingRuleService.class);

    @Override
    public int calculate(AdVO ad) {
        logger.info("Calculating completeness points for ad: {}", ad);

        if (completeChalet(ad) ||
                completeFlat(ad) ||
                completeGarage(ad)) {

            return COMPLETE_FIELDS;
        }

        return 0;
    }

    private boolean completeGarage(AdVO ad) {
        if (!ad.isGarage())
            return false;

        return ad.hasPictures();
    }

    private boolean completeFlat(AdVO ad) {
        if (!ad.isFlat())
            return false;

        return ad.hasPictures() && ad.hasDescription() && ad.hasHouseSize();
    }

    private boolean completeChalet(AdVO ad) {
        if (!ad.isChalet())
            return false;

        return ad.hasPictures() && ad.hasDescription() && ad.hasHouseSize() && ad.hasGardenSize();
    }

}
