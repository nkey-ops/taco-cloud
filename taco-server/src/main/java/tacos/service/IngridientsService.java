package tacos.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tacos.domain.Ingredient;
import tacos.domain.User;

@Service
public class IngridientsService {

  private final RestTemplate restTeplate;
  private final String ingredientsURI;
  private final String JSI = "JSESSIONID=6A82BCCA5C0E4ACB8B1F322600175BBC";

  public IngridientsService(
      @Value("${tacos.service.ingredients-service.uri}") String ingredientsURI) {
    this.restTeplate = new RestTemplate();
    this.ingredientsURI = ingredientsURI;
  }

  public List<Ingredient> get(User user) {
    Objects.requireNonNull(user);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.COOKIE, JSI);

    ResponseEntity<List<Ingredient>> responseEntity =
        restTeplate.exchange(
            ingredientsURI,
            HttpMethod.GET,
            new HttpEntity<>(httpHeaders),
            new ParameterizedTypeReference<List<Ingredient>>() {});

    return responseEntity.getBody();
  }

  public Ingredient save(Ingredient ingredient) {
    Objects.requireNonNull(ingredient);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.COOKIE, JSI);
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    ResponseEntity<Ingredient> responseEntity =
        restTeplate.postForEntity(
            ingredientsURI, new HttpEntity<>(ingredient, httpHeaders), Ingredient.class);

    return responseEntity.getBody();
  }

  public void delete(String ingredientId) {
    Objects.requireNonNull(ingredientId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.COOKIE, JSI);
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

    restTeplate.exchange(
        ingredientsURI + "/" + ingredientId, 
        HttpMethod.DELETE,
        new HttpEntity<>(httpHeaders), String.class);
  }
}
