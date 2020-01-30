package com.distance.app.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.distance.app.domain.Distance;
import com.distance.app.entity.PostCodeEntity;
import com.distance.app.service.PostCodeServiceImpl;
import com.distance.app.util.PostCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Enables facility to manage postcode for UK
 */
@RequestMapping(value = "/uk", consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Slf4j
public class PostCodeController {

    private final PostCodeServiceImpl paymentServiceImpl;

    /**
     * Constructor
     *
     * @param paymentServiceImpl {@link PostCodeServiceImpl}
     */
    public PostCodeController(PostCodeServiceImpl paymentServiceImpl) {
        this.paymentServiceImpl = paymentServiceImpl;
    }

    /**
     * Calculates distance between two postCode
     *
     * @param origin      postCode
     * @param destination postCode
     * @return {@link Distance}
     */
    @GetMapping(value = "/findDistance")
    @ResponseStatus(OK)
    public Distance calculateDistance(@RequestParam(value = "origin") String origin, @RequestParam(value = "destination") String destination) {
        log.info("^^^ Request received to find distance from {} to {}", origin, destination);
        return paymentServiceImpl.calculateDistance(origin, destination);
    }

    /**
     * Retrieves all postCodeDetails
     *
     * @return list of PostCodeEntity
     */
    @GetMapping(value = "/postcodes")
    public List<PostCodeEntity> getAllPostCodeDetails() {
        return paymentServiceImpl.getAllPostCodeDetails();
    }

    /**
     * Retrieves place details
     *
     * @param postcodeOrId location identifier
     * @return PostCodeEntity
     */
    @GetMapping(value = "/postcode/{postcodeOrId}")
    @ResponseStatus(OK)
    public PostCodeEntity getPostCodeDetails(@PathVariable(value = "postcodeOrId") String postcodeOrId) {
        log.info("^^^ Request received to get postcode details for {}", postcodeOrId);
        return paymentServiceImpl.getPostCodeDetails(postcodeOrId);
    }

    /**
     * Updates PostCode details
     *
     * @param postCodeEntity {@link PostCodeEntity}
     * @return {@link PostCodeEntity}
     */
    @PostMapping(value = "/postcode")
    @ResponseStatus(CREATED)
    public PostCodeEntity savePostCodeDetails(@RequestBody @Valid PostCodeEntity postCodeEntity) {
        log.info("^^^ Request received to update postcode details for {}", postCodeEntity.getPostCode());
        return paymentServiceImpl.savePostCodeDetails(postCodeEntity);
    }

    /**
     * Deletes PostCode details
     *
     * @param postcode postcode
     */
    @DeleteMapping(value = "/postcode")
    @ResponseStatus(OK)
    public String deletePostCodeDetails(@RequestParam(value = "postcode", required = false) String postcode,
                    @RequestParam(value = "id", required = false) Long id) throws Exception {
        String message;
        if (!StringUtils.isEmpty(postcode)) {
            paymentServiceImpl.deletePostCodeDetailsByPostCode(postcode);
            message = "Record: " + postcode + " deleted successfully";
        } else if (id != null) {
            paymentServiceImpl.deletePostCodeDetailsById(id);
            message = "Record: " + id + " deleted successfully";
        } else {
            throw new PostCodeException("Pass postcode or Id to delete the postCode Details", "1002");
        }
        log.info("^^^ {}", message);
        return message;
    }

}