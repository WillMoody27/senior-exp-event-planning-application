//package com.cs4360msudenver.ueventspringbootbackend;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//@Configuration
//public class CORSConfig {
//    @Bean
//// This method will allow the frontend to access the backend API - replace the URL with your frontend URL
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.addAllowedOrigin("*"); // Replace with your frontend URL (For Localhost, use http://127.0.0.1:5500)
//        corsConfig.addAllowedMethod("*");
//        corsConfig.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//        return new CorsFilter(source);
//    }
//}
