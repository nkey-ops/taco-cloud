package tacos;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WebConfig.class)
@ActiveProfiles("web-test")
public class WebConfigTest {

	
	
	@Autowired
	private MockMvc mockMvc;


	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/"))
			      .a2ndExpect(status().isOk())
			      .andExpect(view().name("home"))
			      .andExpect(content().string(
			          containsString("Welcome to...")));
	}
	
}
