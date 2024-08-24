package gyus.mybookshelf.config

import gyus.mybookshelf.jwt.JwtRequestFilter
import gyus.mybookshelf.oauth.OAuth2SuccessHandler
import gyus.mybookshelf.service.MyOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

@Configuration
class SecurityConfig {


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtRequestFilter: JwtRequestFilter,
        myOAuth2UserService: MyOAuth2UserService,
        oAuth2SuccessHandler: OAuth2SuccessHandler
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .httpBasic {}
            .oauth2Login {oauth ->
                oauth.userInfoEndpoint { it.userService(myOAuth2UserService) }
                oauth.successHandler(oAuth2SuccessHandler)
            }
//            .sessionManagement {
//                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/bookshelves", "/bookshelves/**").permitAll()
                    .requestMatchers("/", "/login/**").permitAll()
                    .requestMatchers("/authenticate").permitAll()
                    .requestMatchers("/books", "/books/**").permitAll()
                    .requestMatchers("/changeSecretKey").permitAll()
                    .requestMatchers("/hello").hasRole("MEMBER")
                    .requestMatchers("/home").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.GET, "/posts/**").hasAnyRole("MEMBER", "AUTHOR")
                    .requestMatchers(HttpMethod.POST, "/posts/**").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.PUT, "/posts/**").hasRole("AUTHOR")
                    .requestMatchers(HttpMethod.DELETE, "/posts/**").hasRole("AUTHOR")
                    .requestMatchers( "/members/**").hasRole("AUTHOR")
                    .anyRequest().denyAll()
            }
        return http.build()
    }

    @Bean("userSecretKeys")
    fun userSecretKeys(): Map<String, String> {
        val secretKeys = mutableMapOf<String, String>()
        for (i in 1..2) {
            val key = UUID.randomUUID().toString()
            println("key : $key")
            secretKeys["member$i@email.com"] = key
        }
        return secretKeys
    }
}