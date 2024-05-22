package com.fixluck.hogwartartifactsonline.artifact;


import com.fixluck.hogwartartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.fixluck.hogwartartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.fixluck.hogwartartifactsonline.artifact.dto.ArtifactDTO;
import com.fixluck.hogwartartifactsonline.system.Result;
import com.fixluck.hogwartartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    @Autowired
    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId) {
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        //thay vì gửi Object Java thì ta chỉ gửi Dto để loại bỏ bug Infinitive Recursion
        ArtifactDTO artifactDTO = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDTO);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        //Convert foundArtifacts to a list of artifactDto
        List<ArtifactDTO> artifactDtos = foundArtifacts
                .stream()
                .map(foundArtifact ->
                        this.artifactToArtifactDtoConverter.convert(foundArtifact))
                .collect(Collectors.toList());


        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDtos);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDTO artifactDto) {

        //convert artifactdto to artifact
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);

        //convert ngược lại artifact về artifactdto vì ko thể chuyển artifact vào request đc
        ArtifactDTO saveArtifactDTO = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", saveArtifactDTO);

    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDTO artifactDto) {
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updated = this.artifactService.update(artifactId, update);
        ArtifactDTO updatedArtifactDTO = this.artifactToArtifactDtoConverter.convert(updated);

        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDTO);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        this.artifactService.delete(artifactId);

        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
