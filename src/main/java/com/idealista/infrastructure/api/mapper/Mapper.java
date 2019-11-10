package com.idealista.infrastructure.api.mapper;

import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import com.idealista.infrastructure.persistence.AdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Mapper {
    private final static Logger logger = LoggerFactory.getLogger(Mapper.class);

    public static PublicAd mapPublicAd(AdVO ad, List<String> imageUrls) {
        logger.info("Mapping ad {} with pictures {} to public ad", ad, imageUrls);

        return new PublicAd(
                ad.getId(),
                ad.getTypology(),
                ad.getDescription(),
                imageUrls,
                ad.getHouseSize(),
                ad.getGardenSize()
        );
    }

    public static QualityAd mapQualityAd(AdVO ad, List<String> imageUrls) {
        logger.info("Mapping ad {} with pictures {} to quality ad", ad, imageUrls);

        return new QualityAd(
                ad.getId(),
                ad.getTypology(),
                ad.getDescription(),
                imageUrls,
                ad.getHouseSize(),
                ad.getGardenSize(),
                ad.getScore(),
                ad.getIrrelevantSince()
        );
    }
}
