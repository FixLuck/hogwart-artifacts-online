package com.fixluck.hogwartartifactsonline.system;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import com.fixluck.hogwartartifactsonline.artifact.ArtifactRepository;
import com.fixluck.hogwartartifactsonline.hogwartuser.HogwartsUser;
import com.fixluck.hogwartartifactsonline.hogwartuser.UserRepository;
import com.fixluck.hogwartartifactsonline.hogwartuser.UserService;
import com.fixluck.hogwartartifactsonline.wizard.Wizard;
import com.fixluck.hogwartartifactsonline.wizard.WizardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {
    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    private final UserRepository userRepository;

    private final UserService userService;



    @Override
    //hàm đc gọi khi springboot chạy, dùng để test data trước khi client gọi API endpoint
    public void run(String... args) throws Exception {

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");


        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");


        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");


        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");


        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");


        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);


        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);

        Wizard w4 = new Wizard();
        w4.setName("Severus Snape");
        wizardRepository.save(w4);
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("tom");
        u2.setPassword("qwerty");
        u2.setEnabled(false);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("eric");
        u3.setPassword("654321");
        u3.setEnabled(true);
        u3.setRoles("user");

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);

        HogwartsUser u4 = new HogwartsUser();
        u4.setId(1);
        u4.setUsername("max");
        u4.setPassword("123456");
        u4.setEnabled(true);
        u4.setRoles("admin user");

        HogwartsUser u5 = new HogwartsUser();
        u5.setId(5);
        u5.setUsername("harry");
        u5.setPassword("abcdef");
        u5.setEnabled(true);
        u5.setRoles("user");

        HogwartsUser u6 = new HogwartsUser();
        u6.setId(6);
        u6.setUsername("hermione");
        u6.setPassword("qwerty");
        u6.setEnabled(true);
        u6.setRoles("user");

        this.userService.save(u4);
        this.userService.save(u5);
        this.userService.save(u6);

    }
}
