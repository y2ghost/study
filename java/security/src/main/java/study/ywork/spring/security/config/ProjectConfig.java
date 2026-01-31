package study.ywork.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.ywork.spring.security.security.ApiKeyFilter;
import study.ywork.spring.security.security.CustomAuthenticationProvider;

@Configuration
public class ProjectConfig {
    private final CustomAuthenticationProvider authenticationProvider;

    public ProjectConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.authenticationProvider(authenticationProvider);
        http.userDetailsService(userDetailsService());
        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        http.addFilterBefore(new ApiKeyFilter(false), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // @Bean
    UserDetailsService userDetailsService() {
        var user = User.withUsername("dev")
            .password("12345")
            .authorities("read")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    // @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
