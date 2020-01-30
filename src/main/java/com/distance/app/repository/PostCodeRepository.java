package com.distance.app.repository;

import com.distance.app.entity.PostCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for PostCodeEntity
 */
@Repository
public interface PostCodeRepository extends JpaRepository<PostCodeEntity, Long> {

    /**
     * returns PostCodeEntity for given postCode
     *
     * @param postCode postCode
     * @return {@link PostCodeEntity}
     */
    PostCodeEntity findByPostCode(String postCode);

}
