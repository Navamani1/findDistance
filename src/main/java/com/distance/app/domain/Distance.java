package com.distance.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.distance.app.util.DistanceEnum;
import lombok.Data;

/**
 * Model contains distance and place details
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Distance {

    private String originPostCode;
    private double originLatitude;
    private double originLongitude;
    private String originPlace;

    private String destinationPostCode;
    private double destinationLatitude;
    private double destinationLongitude;
    private String destinationPlace;

    private double distance;
    private DistanceEnum unit;
}
