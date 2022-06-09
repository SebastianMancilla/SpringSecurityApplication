package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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


    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder)throws Exception{

        builder.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
        .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
        .authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON(a.user_id = u.id) WHERE u.username=?");

        // PasswordEncoder encoder = passwordEncoder;
        // UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        // try {
        //     builder.inMemoryAuthentication()
        //     .withUser(users.username("admin").password("12345").roles("ADMIN", "user"))
        //     .withUser(users.username("sebas").password("1234").roles("USER"));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
    
}
