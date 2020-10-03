package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
/*@EnableWebSecurity*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource datasource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(org.springframework.security.core.userdetails.User.withUsername("user").password("pass").roles("USER_AUTHORIZED"))

                .and()
                .jdbcAuthentication()
                .dataSource(datasource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select email, password, 'true' from user where email=?")
                .authoritiesByUsernameQuery("select email, 'USER_AUTHORIZED' from user where email=?")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/register", "/actuator/shutdown", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .httpBasic()
        ;

        //http.csrf().ignoringAntMatchers("/api/register", "/actuator/shutdown", "/h2-console/**");
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder encoder() {
        //return NoOpPasswordEncoder.getInstance(); //todo debug only
        return new BCryptPasswordEncoder();
    }

}
