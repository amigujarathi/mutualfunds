package com.pharmerz.config.production;

import com.pharmerz.security.RestCsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.annotation.Resource;

/**
 * Created by ankur on 22-10-2016.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /*
        auth.inMemoryAuthentication()
                .withUser("admin").password("password").roles("ADMIN", "USER")
                .and()
                .withUser("user").password("password").roles("USER");
        */

       /* auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder); */
       auth.authenticationProvider(authenticationProvider());


    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }




    @Configuration
    @Order(1)
    @Profile("production")
    @EnableWebSecurity
    public static class FormWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{

        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;
        @Autowired
        private AuthenticationSuccessHandler authenticationSuccessHandler;
        @Autowired
        private AuthenticationFailureHandler authenticationFailureHandler;
        @Autowired
        private LogoutSuccessHandler logoutSuccessHandler;
        //@Autowired
        private RestCsrfHeaderFilter restCsrfHeaderFilter;



        @Override
        protected void configure(HttpSecurity http) throws Exception {
           http
                   .exceptionHandling()
                   .authenticationEntryPoint(authenticationEntryPoint)
                   .and()
                   .antMatcher("/**")
                   .authorizeRequests()
                   .antMatchers("/").permitAll()
                   .antMatchers("/index.html", "/api/v1/users/search/findByEmail", "/api/v1/users/search/findByUsername", "/api/v1/users/search/findByMobile").permitAll()
                   .antMatchers(HttpMethod.GET,"/api/v1/countries", "/api/v1/products", "/api/v1/categories", "/api/v1/products/search/findByCategoryId","/api/v2/contactus","/api/v2/emails","/api/v2/mobile").permitAll()
                   .antMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/organizations","/api/v1/contactus","/api/v2/emails","/api/v2/verifyemails","/api/v2/mobile","/api/v2/verifymobile","/api/v2/message").permitAll()

                   .anyRequest()
                   .authenticated()
                   .and()
                   .formLogin()
                   .loginProcessingUrl("/login")
                   .permitAll()
                   .successHandler(authenticationSuccessHandler)
                   .failureHandler(authenticationFailureHandler)
                   .and()
                   .logout()
                   .logoutSuccessHandler(logoutSuccessHandler)
                   .invalidateHttpSession(false)
                   .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                   .and()
                   .csrf()
                   .disable();
                   //.and()
                   //.csrf().csrfTokenRepository(csrfTokenRepository())
                   //.and()
                   //.addFilterAfter(restCsrfHeaderFilter, CsrfFilter.class);


        }

        //@Bean
        public CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }





        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/js/**", "/css/**", "/views/**", "/image/**", "/fonts/**", "/app/**");
        }
    }
}
