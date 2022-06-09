package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private LoginSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
        // .antMatchers("/ver/**").hasAnyRole("USER", "ADMIN")
        // .antMatchers("/uploads/**").hasAnyRole("USER","ADMIN")
        // .antMatchers("/form/**").hasAnyRole("ADMIN")
        // .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
        // .antMatchers("/factura/**").hasAnyRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin()
            .successHandler(successHandler)
            .loginPage("/login")
        .permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error_403");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder){

        PasswordEncoder encoder = passwordEncoder();
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        try {
            builder.inMemoryAuthentication()
            .withUser(users.username("admin").password("12345").roles("ADMIN", "user"))
            .withUser(users.username("sebas").password("1234").roles("USER"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
