package com.bifunction.ppmtool.security;

import static com.bifunction.ppmtool.security.SecurityConstants.H2_URL;
import static com.bifunction.ppmtool.security.SecurityConstants.SIGN_UP_URLS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bifunction.ppmtool.services.CustomUserDetailsService;;


/*
 
 @EnableGlobalMethodSecurity : gelecekte eklenebilecek securityler için, method bazlı erişim vs
 
 WebSecurityConfigurerAdapter :  a class that implements WebSecurityConfigurer interface, provides default security configurations
 by extending it , it is going to allow us to customize those security configurations by overriding some of the methods
 
 authenticationEntryPoint : handles what exceptions need to be thrown when they were somebodies not authenticated, 
 So we will customize that by extending it
 
 this is a REST API. So we want to have this to be stateless, we don't want this to save sessions or cookies.
 Because that's why we want to use json token for
 so that the server doesn't have to hold a session and 
 everytime there's our request coming would have valid token then the server is just going to respond
 tokens have expiration dates
 have information about the user but we have no state on the server. Redux hold the state of the application
 
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{	 
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter () {return new JwtAuthenticationFilter();}
	
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// we use authentication manager builder to basically build the authentication manager
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    	authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
    	return super.authenticationManager();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	   http.cors().and().csrf().disable()
	           .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	           .sessionManagement()
	           .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	           .and()
	           .headers().frameOptions().sameOrigin()  // to enable H2 Database
	           .and()
               .authorizeRequests()
               .antMatchers(  // permit these routes right now, we don't need to be logged in 
                       "/",
                       "/favicon.ico",
                       "/**/*.png",
                       "/**/*.gif",
                       "/**/*.svg",
                       "/**/*.jpg",
                       "/**/*.html",
                       "/**/*.css",
                       "/**/*.js"
               ).permitAll()
               .antMatchers(SIGN_UP_URLS).permitAll()
               .antMatchers(H2_URL).permitAll()
               .anyRequest().authenticated(); // anything other than that, we need to authentication
	   
	   http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	           
	}
	
	

}
