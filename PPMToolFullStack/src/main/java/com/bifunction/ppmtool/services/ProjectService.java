package com.bifunction.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bifunction.ppmtool.domain.Backlog;
import com.bifunction.ppmtool.domain.Project;
import com.bifunction.ppmtool.domain.User;
import com.bifunction.ppmtool.exceptions.ProjectIdException;
import com.bifunction.ppmtool.exceptions.ProjectNotFoundException;
import com.bifunction.ppmtool.repositories.BacklogRepository;
import com.bifunction.ppmtool.repositories.ProjectRepository;
import com.bifunction.ppmtool.repositories.UserRepository;

// You always want to have your logic abstracted from the controller as much as you can
// You want the controller to be a router rather than a place that holds your logic
// So use SERVICE LAYER that talks to the repository

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private UserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {

		if (project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in your account");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier()
						+ "' cannot be updated because it doesn't exist");
			}
		}

		try {

			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}

			if (project.getId() != null) {
				project.setBacklog(
						backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}

			return projectRepository.save(project);

		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}

	}

	public Project findByProjectIdentifier(String projectId, String username) {

		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
		}

		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account.");
		}

		return project;
	}

	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}

	public void deleteProjectByIdentifier(String projectId, String username) {
//		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
//		
//		if(project == null) {
//			throw new ProjectIdException("Can not delete project with ID '" + projectId.toUpperCase() + "'. This project doesn't exist");
//		}
//		
		projectRepository.delete(findByProjectIdentifier(projectId, username));
	}

}
