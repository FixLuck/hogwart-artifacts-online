package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.system.Result;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import com.fixluck.hogwartartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.fixluck.hogwartartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import com.fixluck.hogwartartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {
    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    @Autowired
    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;

        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;

        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
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

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDTO wizardDto) {

        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);

        Wizard saveWizard = this.wizardService.save(newWizard);

        WizardDTO saveWizardDto = this.wizardToWizardDtoConverter.convert(saveWizard);

        return new Result(true, StatusCode.SUCCESS, "Add Success", saveWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDTO wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updated = this.wizardService.update(wizardId, update);

        WizardDTO updatedWizardDto = this.wizardToWizardDtoConverter.convert(updated);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success"    );
    }


    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact assignment success");
    }
}
