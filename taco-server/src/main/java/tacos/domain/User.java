package tacos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;
  private String password;
  private String role;

  private String resourceServerUsername;
  private String resourceServerPassword;

  //TODO check limits
  @Column private String clientId;
  @Column(length = 200)
  private String registrationClientUri;

  private String fullname;
  private String street;
  private String city;
  private String state;
  private String zip;
  private String phoneNumber;

  public User(
      String username,
      String password,
      String resourceServerUsername,
      String resourceServerPassword,
      String role,
      String fullname,
      String street,
      String city,
      String state,
      String zip,
      String phoneNumber) {

    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    Objects.requireNonNull(resourceServerUsername);
    Objects.requireNonNull(resourceServerPassword);
    Objects.requireNonNull(role);
    Objects.requireNonNull(fullname);
    Objects.requireNonNull(street);
    Objects.requireNonNull(city);
    Objects.requireNonNull(state);
    Objects.requireNonNull(zip);
    Objects.requireNonNull(phoneNumber);

    this.username = username;
    this.password = password;
    this.resourceServerUsername = resourceServerUsername;
    this.resourceServerPassword = resourceServerPassword;
    this.role = role;

    this.fullname = fullname;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phoneNumber = phoneNumber;
  }

  public User(
      String username,
      String password,
      String resourceServerUsername,
      String resourceServerPassword,
      String role) {

    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    Objects.requireNonNull(resourceServerUsername);
    Objects.requireNonNull(resourceServerPassword);

    this.username = username;
    this.password = password;
    this.resourceServerUsername = resourceServerUsername;
    this.resourceServerPassword = resourceServerPassword;
    this.role = role;
  }

  User() {}

  public Optional<String> getClientId() {
    return Optional.ofNullable(clientId);
  }

  public Optional<String> getRegistrationClientUri() {
    return Optional.ofNullable(registrationClientUri);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority(role));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
