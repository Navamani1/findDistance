package com.distance.app.service;

import static com.distance.app.util.Constants.ID_DESCRIPTION;
import static com.distance.app.util.Constants.ID_ERROR;
import static com.distance.app.util.Constants.POST_CODE_DESCRIPTION;
import static com.distance.app.util.Constants.POST_CODE_ERROR;
import static com.distance.app.util.DistanceEnum.KM;

import com.distance.app.domain.Distance;
import com.distance.app.entity.PostCodeEntity;
import com.distance.app.repository.PostCodeRepository;
import com.distance.app.util.PostCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * Enables postCode management and provides distance calculator
 */
@Service
@Slf4j
public class PostCodeServiceImpl {

    private final static double EARTH_RADIUS = 6371; // radius in kilometers

    private final PostCodeRepository postCodeRepository;

    /**
     * Constructor
     *
     * @param postCodeRepository {@link PostCodeRepository}
     */
    public PostCodeServiceImpl(PostCodeRepository postCodeRepository) {
        this.postCodeRepository = postCodeRepository;
    }

    /**
     * Calculate Distance
     *
     * @param originPostCode      originPostCode
     * @param destinationPostCode destinationPostCode
     * @return {@link Distance}
     */
    public Distance calculateDistance(String originPostCode, String destinationPostCode) {
        PostCodeEntity originEntity = getPostCodeDetails(originPostCode);
        PostCodeEntity destinationEntity = getPostCodeDetails(destinationPostCode);
        double distanceInKM = calculateDistance(originEntity.getLatitude(), originEntity.getLongitude(),
                        destinationEntity.getLatitude(), destinationEntity.getLongitude());

        Distance distance = new Distance();
        distance.setOriginPostCode(originEntity.getPostCode());
        distance.setOriginLatitude(originEntity.getLatitude());
        distance.setOriginLongitude(originEntity.getLongitude());

        distance.setDestinationPostCode(destinationEntity.getPostCode());
        distance.setDestinationLatitude(destinationEntity.getLatitude());
        distance.setDestinationLongitude(destinationEntity.getLongitude());
        distance.setDistance(distanceInKM);
        distance.setUnit(KM);
        log.info("^^^ distance from {} to {} is {}", originPostCode, destinationPostCode, distanceInKM);
        return distance;
    }

    /**
     * Retrieves PostCodeEntity for given PostCode
     *
     * @param postcodeOrId postCode or Id
     * @return {@link PostCodeEntity}
     */
    public PostCodeEntity getPostCodeDetails(String postcodeOrId) {
        PostCodeEntity postCodeEntity;
        if (postcodeOrId.matches("[0-9]+")) {
            postCodeEntity = getPostCodeDetailsById(Long.parseLong(postcodeOrId));
        } else {
            postCodeEntity = getPostCodeDetailsByPostCode(postcodeOrId);
        }
        return postCodeEntity;
    }

    /**
     * Retrieves PostCodeEntity for given PostCode
     *
     * @return {@link PostCodeEntity}
     */
    public List<PostCodeEntity> getAllPostCodeDetails() {
        List<PostCodeEntity> postCodeEntities = postCodeRepository.findAll();
        if (CollectionUtils.isEmpty(postCodeEntities)) {
            throw new PostCodeException("No PostCode details are available", "1002");
        }
        return postCodeEntities;
    }

    /**
     * Updates PostCodeEntity
     *
     * @param postCodeEntity PostCodeEntity
     * @return {@link PostCodeEntity}
     */
    public PostCodeEntity savePostCodeDetails(PostCodeEntity postCodeEntity) {
        return postCodeRepository.save(postCodeEntity);
    }

    /**
     * Deletes PostCodeEntity by postcode
     *
     * @param postCode postCode
     */
    public void deletePostCodeDetailsByPostCode(String postCode) {
        postCodeRepository.delete(getPostCodeDetails(postCode));
    }

    /**
     * Deletes PostCodeEntity by id
     *
     * @param id identifier
     */
    public void deletePostCodeDetailsById(long id) {
        postCodeRepository.delete(getPostCodeDetailsById(id));
    }

    private PostCodeEntity getPostCodeDetailsByPostCode(String postcode) {
        PostCodeEntity postCodeEntity = postCodeRepository.findByPostCode(postcode);
        if (postCodeEntity == null) {
            log.error("^^^ Given postcode is not available {}", postcode);
            throw new PostCodeException(POST_CODE_DESCRIPTION, POST_CODE_ERROR);
        }
        return postCodeEntity;
    }

    private PostCodeEntity getPostCodeDetailsById(long id) {
        Optional<PostCodeEntity> postCodeOptional = postCodeRepository.findById(id);
        if (!postCodeOptional.isPresent()) {
            log.error("^^^ Given postcode Id is not available {}", id);
            throw new PostCodeException(ID_DESCRIPTION, ID_ERROR);
        }
        return postCodeOptional.get();
    }

    private double calculateDistance(double latitude, double longitude, double latitude2, double longitude2) {
        double lon1Radians = Math.toRadians(longitude);
        double lon2Radians = Math.toRadians(longitude2);
        double lat1Radians = Math.toRadians(latitude);
        double lat2Radians = Math.toRadians(latitude2);
        double a = haversine(lat1Radians, lat2Radians) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (EARTH_RADIUS * c);
    }

    private double haversine(double deg1, double deg2) {
        return square(Math.sin((deg1 - deg2) / 2.0));
    }

    private double square(double x) {
        return x * x;
    }
}