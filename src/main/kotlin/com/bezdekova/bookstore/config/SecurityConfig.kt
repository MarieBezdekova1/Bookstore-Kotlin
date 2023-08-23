package com.bezdekova.bookstore.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@SecurityScheme(name = "securityschema", scheme = "basic", type = SecuritySchemeType.HTTP)
class WebSecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .authorizeHttpRequests { requests ->
                    requests
                            .requestMatchers(
                                    //"/api/v1/authors",
                                    "/v3/api-docs/**",
                                    "/api-docs/**",
                                    "/swagger-ui/**",
                                    "swagger/**",
                                    "/swagger-resources/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                }
                .csrf { it.disable() }
                .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun testOnlyUsers(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(user)
    }
}