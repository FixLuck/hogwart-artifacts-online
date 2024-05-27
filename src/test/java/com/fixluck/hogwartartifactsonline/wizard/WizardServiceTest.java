package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.artifact.ArtifactRepository;
import com.fixluck.hogwartartifactsonline.system.exception.ObjectNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;




    List<Artifact> artifacts;
    List<Wizard> wizards;


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

    @Test
    void testFindAll() {
        //Given
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        //When
        List<Wizard> actualWizards = this.wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(this.wizardRepository, times(1)).findAll(); //verify given
    }

    @Test
    void testSave() {
        //Given: create fake data
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");
        newWizard.setId(4);

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);
        //When
        Wizard saveWizard = this.wizardService.save(newWizard);

        //Then
        assertThat(saveWizard.getId()).isEqualTo(4);
        assertThat(saveWizard.getName()).isEqualTo("Hermione Granger");
        verify(this.wizardRepository, times(1)).save(newWizard);


    }

    @Test
    void testUpdateSuccess() {
        //Given: create a fake data the represents an existing wizard in db that we try to update
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard updateWizard = new Wizard();
        updateWizard.setId(1);
        updateWizard.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //Then
        Wizard update = this.wizardService.update(1, updateWizard);

        //When
        assertThat(updateWizard.getId()).isEqualTo(update.getId());
        assertThat(updateWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);


    }

    @Test
    void testUpdateIdNotFound() {
        //Given
        Wizard foundWizard = new Wizard();
        foundWizard.setId(1);
        foundWizard.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //When
        assertThrows(WizardNotFoundException.class, () -> {
            this.wizardService.update(1, foundWizard);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(1);

    }

    @Test
    void testDeleteSuccess() {
        //Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");
        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        //When
        wizardService.delete(1);

        //Then
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).deleteById(1);

    }

    @Test
    void testDeleteWithIdNotFound() {
        //Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //When
        assertThrows(WizardNotFoundException.class, () -> {
            this.wizardService.delete(1);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, never()).deleteById(1);
    }

    @Test
    void testAssignArtifactSuccess() {
        //Given
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");


        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");
        oldWizard.addArtifact(a1);

        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");
        newWizard.setId(4);


        //tìm kiếm xem cả artifact và wizard có tồn tại ko ko, nếu có thì mới có thể
        //gán artifact cho wizard, nếu ko thì throw Exception
        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        given(this.wizardRepository.findById(4)).willReturn(Optional.of(newWizard));

        //When
        this.wizardService.assignArtifact(4, "1250808601744904191");

        //Then
        assertThat(a1.getOwner().getId()).isEqualTo(4);
        assertThat(newWizard.getArtifacts().contains(a1));

    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        //Given
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");


        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");
        oldWizard.addArtifact(a1);



        //tìm kiếm xem cả artifact và wizard có tồn tại ko ko, nếu có thì mới có thể
        //gán artifact cho wizard, nếu ko thì throw Exception
        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //When
        Throwable throwable = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(1, "1250808601744904191");
        });

        //Then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 1 :(");
        assertThat(a1.getOwner().getId()).isEqualTo(1);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        //Given

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());


        //When
        Throwable throwable = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(1, "1250808601744904191");
        });

        //Then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904191 :(");

    }

}