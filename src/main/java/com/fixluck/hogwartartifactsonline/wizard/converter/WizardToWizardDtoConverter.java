package com.fixluck.hogwartartifactsonline.wizard.converter;

import com.fixluck.hogwartartifactsonline.wizard.Wizard;
import com.fixluck.hogwartartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDTO> {

    @Override
    public WizardDTO convert(Wizard source) {
        WizardDTO wizardDTO = new WizardDTO(source.getId(),
                                            source.getName(),
                                            source.getNumberOfArtifacts());
        return wizardDTO;
    }
}
