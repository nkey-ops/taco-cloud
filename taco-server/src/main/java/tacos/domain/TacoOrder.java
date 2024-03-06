package tacos.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TacoOrder implements Serializable {

  private Long id;
  private Date placedAt;

  @NotBlank(message = "Delivery name is required")
  private String deliveryName;

  @NotBlank(message = "Street is required")
  private String deliveryStreet;

  @NotBlank(message = "City is required")
  private String deliveryCity;

  @NotBlank(message = "State is required")
  private String deliveryState;

  @NotBlank(message = "Zip code is required")
  private String deliveryZip;

  @CreditCardNumber(message = "Not a valid credit card number")
  private String ccNumber;

  @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
  private String ccExpiration;

  @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
  private String ccCVV;

  private List<Taco> tacos = new ArrayList<>();

  private User user;

  public TacoOrder(
      @NotBlank(message = "Delivery name is required") String deliveryName,
      @NotBlank(message = "Street is required") String deliveryStreet,
      @NotBlank(message = "City is required") String deliveryCity,
      @NotBlank(message = "State is required") String deliveryState,
      @NotBlank(message = "Zip code is required") String deliveryZip,
      @CreditCardNumber(message = "Not a valid credit card number") String ccNumber,
      @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
          String ccExpiration,
      @Digits(integer = 3, fraction = 0, message = "Invalid CVV") String ccCVV,
      User user) {

    this.deliveryName = deliveryName;
    this.deliveryStreet = deliveryStreet;
    this.deliveryCity = deliveryCity;
    this.deliveryState = deliveryState;
    this.deliveryZip = deliveryZip;
    this.ccNumber = ccNumber;
    this.ccExpiration = ccExpiration;
    this.ccCVV = ccCVV;
    this.user = user;
  }

  public void addTaco(Taco taco) {
    this.tacos.add(taco);
  }
}
