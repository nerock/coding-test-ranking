package com.idealista.application.service.rating.impl;

import com.idealista.application.service.domain.SpecialWords;
import com.idealista.application.service.impl.AdRatingServiceImpl;
import com.idealista.application.service.rating.RatingRuleService;
import com.idealista.infrastructure.persistence.AdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class SpecialWordsRatingRuleService implements RatingRuleService {
    public static final int HAS_WORD = 5;

    private static final String diacriticsPattern = "\\p{InCombiningDiacriticalMarks}+";

    private final Logger logger = LoggerFactory.getLogger(SpecialWordsRatingRuleService.class);

    @Override
    public int calculate(AdVO ad) {
        logger.info("Calculating special words points for ad: {}", ad);

        int score = 0;

        if (!ad.hasDescription()) {
           return 0;
        }

        String formattedDescription = formatDescription(ad.getDescription());

        for (SpecialWords specialWord : SpecialWords.values()) {
            if (formattedDescription.contains(specialWord.name())) {
                score += HAS_WORD;
            }
        }

        return score;
    }

    private String formatDescription(String description) {
        return Normalizer
                .normalize(description, Normalizer.Form.NFD)
                .replaceAll(diacriticsPattern, "")
                .toUpperCase();
    }
}
