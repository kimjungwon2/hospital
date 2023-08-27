package site.hospital.common;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.hospital.common.jwt.JwtAccessDeniedHandler;
import site.hospital.common.jwt.JwtAuthenticationEntryPoint;
import site.hospital.common.jwt.JwtSecurityConfig;
import site.hospital.common.jwt.TokenProvider;
import site.hospital.common.oauth.CustomOAuth2UserService;
import site.hospital.common.oauth.OAuth2AuthenticationFailureHandler;
import site.hospital.common.oauth.OAuth2AuthenticationSuccessHandler;
import site.hospital.common.oauth.OAuth2HttpRequestRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private static final String DEPLOYMENT_IP_ADDRESS = "http://3.37.47.173";

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    private static final String[] PUBLIC_URI = {
            "/api/check",
            "/api/login",
            "/api/signup",
            "/api/search/hospital/**",
            "/api/search/review/**",
            "/api/hospital/view/**",
            "/api/hospital/staffHosInfo/**",
            "/api/hospital/review/**",
            "/api/hospital/question/**",
            "/api/oauth2/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()


                //exception 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                //세션을 설정 안 해서 추가.
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 리소스 별 허용 범위 설정
                .authorizeRequests()
                .antMatchers(PUBLIC_URI).permitAll()
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/staff/**").hasAnyRole("MANAGER")
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/api/oauth2/authorize")
                .authorizationRequestRepository(oAuth2HttpRequestRepository())

                .and()
                .redirectionEndpoint()
                .baseUri("/api/login/oauth2/code/**")

                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)

                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)

                //만들었던 JwtFilter 적용.
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Bean
    public OAuth2HttpRequestRepository oAuth2HttpRequestRepository(){
        return new OAuth2HttpRequestRepository();
    }

    @Override
    public void configure(WebSecurity web) {
        StrictHttpFirewall firewall = new StrictHttpFirewall();

        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        firewall.setAllowSemicolon(true);

        // Allow UTF-8 values
        Pattern allowed = Pattern.compile("[\\p{IsAssigned}&&[^\\p{IsControl}]]*");
        firewall.setAllowedHeaderValues( header -> {
            String parsed = new String(header.getBytes(ISO_8859_1), UTF_8);
            return allowed.matcher(parsed).matches();
        });

        web.httpFirewall(firewall);
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(DEPLOYMENT_IP_ADDRESS);
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
