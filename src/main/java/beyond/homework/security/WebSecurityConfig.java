package beyond.homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Value("${security.enabled}")
    private boolean securityEnabled;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final UserDetailsService jwtUserDetailsService;

    private final RequestFilter requestFilter;

    @Autowired
    public WebSecurityConfig(AuthenticationExceptionHandler authenticationExceptionHandler, UserDetailsService jwtUserDetailsService, RequestFilter requestFilter)
    {
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.requestFilter = requestFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(this.jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        if (!this.securityEnabled)
        {
            System.out.println("Authentication disabled");
            httpSecurity.csrf()
                    .disable();
        }
        else
        {
            System.out.println("Authentication enabled");
            httpSecurity.csrf()
                    .disable()
                    .cors()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/login", "/h2-console/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(this.authenticationExceptionHandler)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            httpSecurity.addFilterBefore(this.requestFilter, UsernamePasswordAuthenticationFilter.class);

        }
        httpSecurity.headers()
                .frameOptions()
                .disable();
    }
}
