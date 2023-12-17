package com.cs4360msudenver.ueventspringbootbackend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity (you should enable it in production)
                .cors().and()
                .authorizeRequests()
//                We added matchers for the endpoints we want to allow access to without authentication
                .antMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/events/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/events/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/events/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/events/**").permitAll()
//                IMAGES UPLOAD + DISPLAY
                .antMatchers(HttpMethod.POST, "/images/**").permitAll()
                .antMatchers(HttpMethod.POST, "/images/uploadImage/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()

//                END OF IMAGES UPLOAD + DISPLAY
//                .anyRequest().authenticated() // Require authentication for any endpoint that's not specified above
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(); // Use Basic Authentication
        http.cors();
    }


    //    //  added  this method to allow access to the authentication manager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
    // CORS Configuration if not using Spring Boot autoconfiguration
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
