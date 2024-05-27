package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.artifact.ArtifactRepository;
import com.fixluck.hogwartartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    @Autowired
    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public Wizard findById(Integer wizardId) {
        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }


    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard) {
        Wizard saveWizard = this.wizardRepository.save(newWizard);
        return saveWizard;
    }

    public Wizard update(Integer wizardId, Wizard updateWizard) {
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(updateWizard.getName());
                    Wizard updatedWizard = this.wizardRepository.save(oldWizard);
                    return updatedWizard;
                })
                .orElseThrow(() -> new WizardNotFoundException(wizardId));

    }

    public void delete(Integer wizardId) {
        Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));

        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        //Find this artifact by Id from DB
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        //Find the wizard by Id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        //Artifact Assignment
        //first we need to check if this artifact is belong to someone
        //we have to remove artifact from the old owner
        //and then resign it to the new owner
        if (artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }

        wizard.addArtifact(artifactToBeAssigned);
    }
}
