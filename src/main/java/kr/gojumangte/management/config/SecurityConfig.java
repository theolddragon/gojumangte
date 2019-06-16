package kr.gojumangte.management.config;

import kr.gojumangte.management.accounts.handler.FormAuthenticationFailureHandler;
import kr.gojumangte.management.accounts.handler.FormAuthenticationSuccessHandler;
import kr.gojumangte.management.accounts.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;

  private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
  private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        "/css/**",
        "/js/**",
        "/img/**",
        "/vendor/**"
    );
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.formLogin()
          .loginPage("/login")
          .loginProcessingUrl("/login-process")
          .successHandler(formAuthenticationSuccessHandler)
          .failureHandler(formAuthenticationFailureHandler)
          .usernameParameter("loginId")
          .passwordParameter("password")
        .and()
          .logout()
          .invalidateHttpSession(true)
          .logoutUrl("/logout")
          .deleteCookies("JSESSIONID")
          .logoutSuccessHandler(simpleUrlLogoutSuccessHandler())
        .and()
          .headers() .cacheControl()
        .and()
          .xssProtection()
        .and()
          .frameOptions().sameOrigin()
        .and()
          .csrf().disable();
  }

  @Bean
  protected SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler(){
    SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
    simpleUrlLogoutSuccessHandler.setTargetUrlParameter("r");
    return simpleUrlLogoutSuccessHandler;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }
}
