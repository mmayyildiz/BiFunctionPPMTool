package com.bifunction.ppmtool.web;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bifunction.ppmtool.domain.Project;
import com.bifunction.ppmtool.services.MapValidationErrorService;
import com.bifunction.ppmtool.services.ProjectService;

// our API
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	// ResponseEntity is a type that allows us to have more control on our JSON Responses
	// BindingResult invokes the validator on an object
	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){
		
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		
		if(errorMap!=null) return errorMap;
		
		Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
	    return new ResponseEntity<Project> (project1, HttpStatus.CREATED);	
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
		
		Project project = projectService.findByProjectIdentifier(projectId, principal.getName());
		return new ResponseEntity<Project> (project, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public Iterable<Project> getAllProjects(Principal principal){
		return projectService.findAllProjects(principal.getName());
	}
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProjectByIdentifier(@PathVariable String projectId, Principal principal) {
	
		projectService.deleteProjectByIdentifier(projectId, principal.getName());
		
		return new ResponseEntity<String>("Project ID with '" + projectId.toUpperCase() + "' was deleted.", HttpStatus.OK);
	}

	
}
