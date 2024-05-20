package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.system.Result;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import com.fixluck.hogwartartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import com.fixluck.hogwartartifactsonline.wizard.dto.WizardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    @Autowired
    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;

    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard wizard = this.wizardService.findById(wizardId);
        WizardDTO wizardDTO = this.wizardToWizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDTO);
    }

    @GetMapping
    public Result findAllWizard() {
        List<Wizard> foundWizards = this.wizardService.findAll();
        List<WizardDTO> wizardDtos = foundWizards
                .stream()
                .map(foundWizard ->
                        this.wizardToWizardDtoConverter.convert(foundWizard))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }
}
