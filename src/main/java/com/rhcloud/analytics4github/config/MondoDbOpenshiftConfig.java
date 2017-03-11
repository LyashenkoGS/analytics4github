package com.rhcloud.analytics4github.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;
import java.util.Collections;

/**
 * Setting to connect to MongoDb 2.4 on openshift.com
 *
 * @author lyashenkogs.
 * @since 8/31/16
 */
@Profile("openshift")
@Configuration
public class MondoDbOpenshiftConfig {

    @Value("${mongodb_db_password}")
    private String password;

    @Value("${mongodb_db_host}")
    private String host;

    @Value("${mongodb_db_username}")
    private String username;

    @Value("${mongodb_database}")
    private String database;

    @Value("${mongodb_db_port}")
    private String port;

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        char[] passwordChar = password.toCharArray();
        int openshiftMongoDbPort = Integer.parseInt(port);
        return getMongoTemplate(host, openshiftMongoDbPort, database, database, username, passwordChar);
    }

    private MongoTemplate getMongoTemplate(String host, int port,
                                           String authenticationDB,//TODO: is it redundant ?
                                           String database,
                                           String user, char[] password)
            throws UnknownHostException {
        return new MongoTemplate(
                new SimpleMongoDbFactory(
                        new MongoClient(
                                new ServerAddress(host, port),
                                Collections.singletonList(
                                        MongoCredential.createCredential(
                                                user,
                                                authenticationDB,
                                                password
                                        )
                                )
                        ),
                        database
                )
        );
    }

}
