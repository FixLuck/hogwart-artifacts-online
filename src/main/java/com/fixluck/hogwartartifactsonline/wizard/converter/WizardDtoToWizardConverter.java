package com.fixluck.hogwartartifactsonline.wizard.converter;

import com.fixluck.hogwartartifactsonline.wizard.Wizard;
import com.fixluck.hogwartartifactsonline.wizard.dto.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDTO, Wizard> {
    @Override
    public Wizard convert(WizardDTO source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());
        wizard.setNumberOfArtifacts(source.numberOfArtifacts());

        return wizard;
    }
}
