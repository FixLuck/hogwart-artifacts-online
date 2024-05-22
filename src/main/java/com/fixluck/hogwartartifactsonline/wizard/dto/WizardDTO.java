package com.fixluck.hogwartartifactsonline.wizard.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDTO(Integer id,

                        @NotEmpty(message = "name is required.")
                        String name,

                        Integer numberOfArtifacts) {
}
