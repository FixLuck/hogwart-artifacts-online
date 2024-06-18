package com.fixluck.hogwartartifactsonline.artifact;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * Annotation DisplayName and Tag are optional
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Artifact API endpoint")
@Tag("integration")
public class ArtifactControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseUrl + "/users/login")
                        .with(httpBasic("max", "123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token"); //Dont forget to add "Bearer " as a prefix
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllArtifactSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(11)));
    }

//    @Test
//    @DisplayName("Check add artifact with valid input (POST)")
//    void testAddArtifactSuccess() throws Exception {
//        Artifact a = new Artifact();
//        a.setName("The Sorcerer's Stone (Philosopher's Stone)");
//        a.setDescription("The Sorcerer's Stone, made by Nicolas Flamel, grants immortality and turns metal into gold. Hidden at Hogwarts to protect it from Voldemort, it is ultimately destroyed to prevent misuse.");
//        a.setImageUrl("ImageUrl");
//
//        String json = this.objectMapper.writeValueAsString(a);
//        this.mockMvc.perform(post(this.baseUrl +"/artifacts")
//                .contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(json).accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(true))
//                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Add Success"))
//                .andExpect(jsonPath("$.data.id").isNotEmpty())
//                .andExpect(jsonPath("$.data.name").value("The Sorcerer's Stone (Philosopher's Stone)"))
//                .andExpect(jsonPath("$.data.description").value("The Sorcerer's Stone, made by Nicolas Flamel, grants immortality and turns metal into gold. Hidden at Hogwarts to protect it from Voldemort, it is ultimately destroyed to prevent misuse."))
//                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(true))
//                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Find All Success"))
//                .andExpect(jsonPath("$.data").value(Matchers.hasSize(11)));
//
//    }
}
