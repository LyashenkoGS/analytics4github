package com.rhcloud.analytics4github.repository;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author lyashenkogs.
 */
public interface RequestToApiRepository extends MongoRepository<RequestToAPI, String> {
    List<RequestToAPI> findByRepository(String repository);
}
