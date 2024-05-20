package com.fixluck.hogwartartifactsonline.wizard;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    @Autowired
    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public Wizard findById(Integer wizardId) {
        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }


    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }
}