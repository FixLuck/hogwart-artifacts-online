package com.fixluck.hogwartartifactsonline.wizard;

public class WizardNotFoundException extends RuntimeException{
    public WizardNotFoundException(Integer id) {
        super("Could not find wizard with Id " + id + " :(");
    }
}
