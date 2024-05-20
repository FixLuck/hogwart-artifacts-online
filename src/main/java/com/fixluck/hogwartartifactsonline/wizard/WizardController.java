package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.system.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
    private final WizardService wizardService;

    @Autowired
    public WizardController(WizardService wizardService) {
        this.wizardService = wizardService;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        return null;
    }
}
