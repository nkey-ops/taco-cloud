package tacos.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Component
@ConfigurationProperties(prefix="taoc.orders")
@Data
@Validated
public class OrderProps {
	
	@Max(value = 25, message="must be bettween [5, 25)")
	@Min(value = 5,  message="must be bettween [5, 25)")
	private int pageSize = 20;
	
}
