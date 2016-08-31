package com.rhcloud.analytics4github.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Setting to connect to MongoDb on openshift.com
 * @author lyashenkogs.
 * @since 8/31/16
 */
@Configuration
public class MondoDbOpenshiftConfig {
    private static Logger LOG = LoggerFactory.getLogger(MondoDbOpenshiftConfig.class);
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        if (System.getenv("OPENSHIFT_MONGODB_DB_HOST") != null) {

            LOG.info("Connecting to OpenShift Mongo");

            String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
            int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
            String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
            String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
            MongoClient mongo = new MongoClient(openshiftMongoDbHost, openshiftMongoDbPort);
            UserCredentials userCredentials = new UserCredentials(username, password);
            String databaseName = System.getenv("OPENSHIFT_APP_NAME");
            MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials);
            MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
            return mongoTemplate;

        } else {

            LOG.info("Connecting to test Mongo");

            return new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "test"));
        }
    }

}
