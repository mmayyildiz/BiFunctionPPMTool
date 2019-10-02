package com.bifunction.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bifunction.ppmtool.domain.Backlog;
import com.bifunction.ppmtool.domain.Project;
import com.bifunction.ppmtool.exceptions.ProjectIdException;
import com.bifunction.ppmtool.repositories.BacklogRepository;
import com.bifunction.ppmtool.repositories.ProjectRepository;

// You always want to have your logic abstracted from the controller as much as you can
// You want the controller to be a router rather than a place that holds your logic
// So use SERVICE LAYER that talks to the repository

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	public Project saveOrUpdateProject(Project project) {
	 
        try {
        	project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
        	
        	if(project.getId() == null) {
            	Backlog backlog = new Backlog();
            	project.setBacklog(backlog);
            	backlog.setProject(project);
            	backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
        	}else {
         	   project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));

        	}
        	
        	return projectRepository.save(project);
        }catch(Exception e) {
        	throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' has already exists" );
        }
		
	}
	
	public Project findByProjectIdentifier(String projectId) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(){
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Can not delete project with ID '" + projectId.toUpperCase() + "'. This project doesn't exist");
		}
		
		projectRepository.delete(project);
	}

}

