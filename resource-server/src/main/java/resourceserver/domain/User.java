package resourceserver.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.util.Arrays;
import java.util.Collection;
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

  private final String username;
  private final String password;
  private final String role;
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
      String fullname,
      String street,
      String city,
      String state,
      String zip,
      String phoneNumber) {

    this.username = username;
    this.password = password;
    this.role = "ROLE_USER";
    this.fullname = fullname;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phoneNumber = phoneNumber;
  }

  public User(String username, String password, String role, GrantedAuthority... authorities) {
    this.password = password;
    this.username = username;
    this.role = role;

    this.authorities = new String[authorities.length];
    for (int i = 0; i < authorities.length; i++) {
      this.authorities[i] = authorities[i].getAuthority();
    }
  }

  public User(String username, String password, String role, String... authorities) {
    this.password = password;
    this.username = username;
    this.role = role;
    this.authorities = authorities.clone();
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
}
