package com.fixluck.hogwartartifactsonline.wizard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import com.fixluck.hogwartartifactsonline.system.exception.ObjectNotFoundException;
import com.fixluck.hogwartartifactsonline.wizard.dto.WizardDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    @Test
    void testSaveWizardSuccess() throws Exception {
        //given:fake json get from front-end
        WizardDTO wizardDto = new WizardDTO(null, "Hermione Granger", null);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard saveWizard = new Wizard();
        saveWizard.setId(4);
        saveWizard.setName("Hermione Granger");
        saveWizard.setNumberOfArtifacts(0);

        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(saveWizard);

        //when and then
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/wizards")
                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(saveWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(saveWizard.getNumberOfArtifacts()));

    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        //Given
        WizardDTO wizardDto = new WizardDTO(1, "Albus Dumbledore", null);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Albus Dumbledore - update");

        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/wizards/1")
                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));

    }

    @Test
    void testUpdateWizardWithIdNotFound() throws Exception {
        //Given
        WizardDTO wizardDto = new WizardDTO(1, "Albus Dumbledore", null);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willThrow(new WizardNotFoundException(1));

        //When and Then
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/wizards/1")
                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).delete(1);

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardWithIdNotFound() throws Exception {
        //Given
        doThrow(new WizardNotFoundException(1))
                .when(this.wizardService).delete(1);

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).assignArtifact(2, "1250808601744904192");

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/wizards/2/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact assignment success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
        //Given
       doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).assignArtifact(5, "1250808601744904192");

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/wizards/5/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904190")).when(this.wizardService).assignArtifact(2   , "1250808601744904190");

        //When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/wizards/2/artifacts/1250808601744904190").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904190 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}