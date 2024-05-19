package com.fixluck.hogwartartifactsonline.artifact.converter;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.artifact.dto.ArtifactDTO;
import com.fixluck.hogwartartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDTO> {

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    @Autowired
    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDTO convert(Artifact source) {
        ArtifactDTO artifactDTO = new ArtifactDTO(source.getId(),
                                                  source.getName(),
                                                  source.getDescription(),
                                                  source.getImageUrl(),
                                                  source.getOwner() != null
                                                            ? this.wizardToWizardDtoConverter.convert(source.getOwner())
                                                            : null);
        // Kiểm tra nếu source có owner (chủ sở hữu)
        // thì sử dụng wizardToWizardDtoConverter để chuyển đổi owner từ Wizard sang WizardDTO.
        // Nếu không, giá trị của thuộc tính này sẽ là null.

//        Cách viết 2 nếu ko hiểu toán tử 3 ngôi
//        WizardDTO ownerDto = null;
//        if (source.getOwner() != null) {
//            ownerDto = this.wizardToWizardDtoConverter.convert(source.getOwner());
//        }
//
//        ArtifactDTO artifactDTO = new ArtifactDTO(source.getId(),
//                source.getName(),
//                source.getDescription(),
//                source.getImageUrl(),
//                ownerDto);
//
//        return artifactDTO;

        return artifactDTO;
    }
}
