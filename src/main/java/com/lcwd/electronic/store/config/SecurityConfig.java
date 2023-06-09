package com.lcwd.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//       UserDetails normal =  User.builder()
//                .username("Ankit")
//                .password(passwordEncoder().encode("ankit"))
//                .roles("NORMAL")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("Nishtha")
//                .password(passwordEncoder().encode("nishtha"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normal, admin);
//
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("login.html")
                .loginProcessingUrl("/process-url")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/error")
                .and()
                .logout()
                .logoutUrl("/logout");

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
