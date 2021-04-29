package com.gmail.kirilllapitsky.finnhub.security.config;

import com.gmail.kirilllapitsky.finnhub.security.JwtAuthenticationFilter;
import com.gmail.kirilllapitsky.finnhub.security.JwtAuthorizationFilter;
import com.gmail.kirilllapitsky.finnhub.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Profile({"default", "test"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userDetailsService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService))
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/paypal/payment/pay").fullyAuthenticated()
                .antMatchers("/api/user/changePassword").fullyAuthenticated()
                .antMatchers("/api/paypal/payment/complete").permitAll()
                .antMatchers("/api/tracking/getAllCompanies").hasAnyAuthority("VIEWING", "TRACKING")
                .antMatchers("/api/tracking/**").hasAuthority("TRACKING")
                .antMatchers("/api/admin/**").hasAuthority("ADMINISTRATE");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}