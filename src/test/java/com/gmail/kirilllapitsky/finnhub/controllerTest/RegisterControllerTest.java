package com.gmail.kirilllapitsky.finnhub.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.controller.RegisterController;
import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.service.RegisterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test-controller")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    private RegisterRequest registerRequest;

    @Before
    public void before() {
        registerRequest = TestData.getRegisterRequest();
    }

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String registerRequestAsString = objectWriter.writeValueAsString(registerRequest);

        Mockito.doNothing().when(registerService).signUp(registerRequest);
        this.mockMvc.perform(post("/api/register").contentType(APPLICATION_JSON_UTF8)
                .content(registerRequestAsString)).andExpect(status().isOk());
    }
}
