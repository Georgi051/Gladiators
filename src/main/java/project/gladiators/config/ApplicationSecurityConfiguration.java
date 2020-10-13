package project.gladiators.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/img/**").permitAll()
                .antMatchers("/articles", "/articles/*", "/about", "/trainers").permitAll()
                .antMatchers("/", "/users/login", "/users/register").anonymous()
                .antMatchers("/articles/delete/**").hasAnyRole("MODERATOR", "ROOT")
                .antMatchers("/products/**").hasAnyRole("MODERATOR","ROOT")
                .antMatchers("/categories/**").hasAnyRole("MODERATOR", "ROOT")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "ROOT")
                .antMatchers("/trainers/confirmation").hasRole("TRAINER_UNCONFIRMED")
                .antMatchers("/trainers/**").hasRole("TRAINER_CONFIRMED")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home",true)
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/home");
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}
