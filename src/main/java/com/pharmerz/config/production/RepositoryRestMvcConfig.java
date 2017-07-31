package com.pharmerz.config.production;

import com.pharmerz.server.domain.model.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Created by ankur on 22-10-2016.
 */
@Configuration
public class RepositoryRestMvcConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        config.exposeIdsFor(
                Role.class,
                User.class,
                UserRole.class




        );


    }



}
