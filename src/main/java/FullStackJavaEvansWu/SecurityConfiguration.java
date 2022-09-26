package FullStackJavaEvansWu;
 
//import java.util.Locale;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

 
@Configuration
public class SecurityConfiguration {
	
	
//	
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	
    	 http.authorizeRequests()
         .antMatchers("/").hasAnyAuthority("USER", "ADMIN")
         .antMatchers("/new").hasAnyAuthority("ADMIN", "USER")
         .antMatchers("/edit/**").hasAnyAuthority("ADMIN")
         .antMatchers("/delete/**").hasAuthority("ADMIN")
         .antMatchers("/admin").hasAuthority("ADMIN")
         .anyRequest().authenticated()
         .and()
         .formLogin().permitAll()
         .and()
         .logout().permitAll()
         .and()
         .exceptionHandling().accessDeniedPage("/403")
         ;
    	 
    	 
    	 http.headers().frameOptions().sameOrigin();
    	 
    	 return http.build();
        
    }
 
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
 
}