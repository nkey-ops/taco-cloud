package resourceserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class User implements UserDetails {

  @Serial
  @Setter(value = AccessLevel.NONE)
  @Getter(value = AccessLevel.NONE)
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private final String username;

  @Column(nullable = false)
  private final String password;

  @Column private String clientId;

  @Column(length = 2048)
  private String registrationAccessToken;

  private String fullname;
  private String street;
  private String city;
  private String state;
  private String zip;
  private String phoneNumber;
  private String[] authorities = {};

  public User(
      String username,
      String password,
      String role,
      String fullname,
      String street,
      String city,
      String state,
      String zip,
      String phoneNumber) {

    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    Objects.requireNonNull(role);
    Objects.requireNonNull(fullname);
    Objects.requireNonNull(street);
    Objects.requireNonNull(city);
    Objects.requireNonNull(state);
    Objects.requireNonNull(zip);
    Objects.requireNonNull(phoneNumber);

    this.username = username;
    this.password = password;
    this.fullname = fullname;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phoneNumber = phoneNumber;

    this.authorities = new String[] {role};
  }

  public User(String username, String password, String role, String... authorities) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    Objects.requireNonNull(authorities);
    Objects.requireNonNull(role);

    this.password = password;
    this.username = username;
    this.authorities = new String[authorities.length + 1];

    for (int i = 0; i < authorities.length; i++) {
      this.authorities[i] = authorities[i];
    }
    this.authorities[authorities.length] = role;
  }

  public Optional<String> getClientId() {
    return Optional.ofNullable(clientId);
  }

  public Optional<String> getRegistrationAccessToken() {
    return Optional.ofNullable(registrationAccessToken);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList();
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

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
  }
}
