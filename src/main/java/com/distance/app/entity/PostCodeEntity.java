package com.distance.app.entity;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Post Codes Entity for UK
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "UK_POST_CODES")
public class PostCodeEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "PostCode is mandatory")
    @Column(name = "POSTCODE")
    private String postCode;

    @Column(name = "LATITUDE")
    private double latitude;

    @Column(name = "LONGITUDE")
    private double longitude;

}
