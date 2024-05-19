package com.fixluck.hogwartartifactsonline.artifact;

import com.fixluck.hogwartartifactsonline.artifact.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    @Autowired
    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId) {
        return this.artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact updateArtifact) {
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(updateArtifact.getName());
                    oldArtifact.setDescription(updateArtifact.getDescription());
                    oldArtifact.setImageUrl(updateArtifact.getImageUrl());
                    Artifact updatedArtifact = this.artifactRepository.save(oldArtifact);
                    return updatedArtifact;
                })
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));

        //vì ko biết artifact đã được update field nào nên ta cần set lại tất cả các field trừ Id

    }

    public void delete(String artifactId) {
        //find if the artifactId exists or not
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
