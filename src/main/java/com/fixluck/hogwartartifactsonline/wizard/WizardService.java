package com.fixluck.hogwartartifactsonline.wizard;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    @Autowired
    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public Wizard findById(Integer wizardId) {
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
        return wizard;
    }
}
