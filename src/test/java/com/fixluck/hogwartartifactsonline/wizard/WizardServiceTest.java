package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;


    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindById() {
        //Given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");
        w.setArtifacts(artifacts);
        w.setNumberOfArtifacts(artifacts.size());

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));

        //When
        Wizard wizard = wizardService.findById(1);

        //Then
        assertThat(wizard.getId()).isEqualTo(w.getId());
        assertThat(wizard.getName()).isEqualTo(w.getName());
        assertThat(wizard.getNumberOfArtifacts()).isEqualTo(w.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class)))
                .willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            Wizard wizard = wizardService.findById(1);
        });

        //Then
        assertThat(throwable)
                .isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizard with Id 1 :(");

        verify(wizardRepository, times(1)).findById(1);

    }
}