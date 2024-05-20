package com.fixluck.hogwartartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;


    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>(); //tránh null array

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        wizards.add(w3);



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //Given
        given(wizardService.findById(1)).willReturn(this.wizards.get(0));

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards/1")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));

    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        //Given
        given(this.wizardService.findById(1)).willThrow(new WizardNotFoundException(1));

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllWizardSuccess() throws Exception {
        //Given
        given(this.wizardService.findAll()).willReturn(this.wizards);
        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"));




    }
}