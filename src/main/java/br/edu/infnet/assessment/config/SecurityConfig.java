//package br.edu.infnet.assessment.config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public InMemoryUserDetailsManager users() {
//        UserDetails teacher = User.withUsername("teacher").password("{noop}123").roles("TEACHER").build();
//        return new InMemoryUserDetailsManager(teacher);
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated());
//        http.httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
//}
