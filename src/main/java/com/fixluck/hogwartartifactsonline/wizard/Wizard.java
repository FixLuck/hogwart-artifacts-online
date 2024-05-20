package com.fixluck.hogwartartifactsonline.wizard;

import com.fixluck.hogwartartifactsonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {


    @Id
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts = new ArrayList<>();

    public Integer numberOfArtifacts;

    public Wizard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this); // giải thích artifact thuộc về wizard nào
        this.artifacts.add(artifact); // add artifact vào danh sách artifact của owner
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void setNumberOfArtifacts(Integer numberOfArtifacts) {
        this.numberOfArtifacts = numberOfArtifacts;
    }
}
