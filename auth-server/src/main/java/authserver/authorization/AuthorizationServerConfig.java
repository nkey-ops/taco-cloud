package authserver.authorization;

import jakarta.servlet.annotation.WebFilter;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AuthorizationServerConfig {

  @Bean
  @Order(1)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    // facilitating a login process upon hitting the endpoint without being logged in
    http.authorizeHttpRequests(r -> r.requestMatchers("/oauth2/authorize/**").authenticated());

    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

    http.exceptionHandling(
            (exceptions) ->
                exceptions.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
        .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .oidc(
            oidc ->
                oidc.clientRegistrationEndpoint(Customizer.withDefaults())
                    .providerConfigurationEndpoint(Customizer.withDefaults()));

    return http.oauth2ResourceServer(
            (resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            r ->
                r.requestMatchers(
                        "/actuator/**",
                        "/register",
                        "/error",
                        "/resources/**",
                        "/**",
                        "/resources/static/**",
                        "/resources/templates/**",
                        "/resources/templates")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(login -> login.loginPage("/login").permitAll())
        .csrf(c -> c.disable())
        .build();
  }

  @Bean
  InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    return new InMemoryUserDetailsManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
    RegisteredClient registeredClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("taco-admin-client")
            .clientSecret(passwordEncoder.encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://127.0.0.1:9000/login/oauth2/code/taco-admin-client")
            .scope("writeIngredients")
            .scope("deleteIngredients")
            .scope("readIngredients")
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.OPENID)
            .build();

    RegisteredClient resourceServerClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("resource_client")
            .clientSecret(passwordEncoder.encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://127.0.0.1:9999/login/oauth2/code/resource_client")
            .scope("openid")
            .build();

    RegisteredClient registrarClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("r-c-registrar")
            .clientSecret(passwordEncoder.encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope("client.create")
            .scope("client.read")
            .build();

    return new InMemoryRegisteredClientRepository(
        registeredClient, resourceServerClient, registrarClient);
  }

  @Bean
  public CommonsRequestLoggingFilter logFilter() {

    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    @WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
    class RFilter extends CommonsRequestLoggingFilter {}

    var filter = new RFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(true);
    filter.setBeforeMessagePrefix("BEFORE:" + System.lineSeparator());
    filter.setAfterMessagePrefix("AFTER: " + System.lineSeparator());
    filter.setIncludeClientInfo(true);
    return filter;
  }

  @Bean
  public JwtAuthenticationConverter jAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrandConverter = new JwtGrantedAuthoritiesConverter();

    jwtGrandConverter.setAuthorityPrefix("");

    var jwtAuthenConverter = new JwtAuthenticationConverter();
    jwtAuthenConverter.setJwtGrantedAuthoritiesConverter(jwtGrandConverter);
    return jwtAuthenConverter;
  }
}
