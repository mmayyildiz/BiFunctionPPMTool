package com.bifunction.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bifunction.ppmtool.domain.Backlog;
import com.bifunction.ppmtool.domain.Project;
import com.bifunction.ppmtool.domain.ProjectTask;
import com.bifunction.ppmtool.exceptions.ProjectNotFoundException;
import com.bifunction.ppmtool.repositories.BacklogRepository;
import com.bifunction.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectService projectService;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

			// PTs to be added to a specific project, project != null, BL exists
			Backlog backlog = projectService.findByProjectIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
			// set bl to pt
			projectTask.setBacklog(backlog);
			// we want our project sequence to be like this : IDPRO-1 IDPRO-2
			Integer backlogSequence = backlog.getPTSequence();
			// update the BL SEQUENCE
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);
			// Add Sequence to Project Task
			projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			// INITIAL PRIORITY when priority null
			if (projectTask.getPriority() == null || projectTask.getPriority() == 0) { // int the future we need projectTask.getPriority == 0 to handle
														// the form
				projectTask.setPriority(3);
			}
			// INITIAL status when status is null
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);
		
	}

	public Iterable<ProjectTask> findBacklogId(String id, String username) {
		
		projectService.findByProjectIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		projectService.findByProjectIdentifier(backlog_id, username);

		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}

		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException(
					"Project Task '" + pt_id + "' does not exist in project: '" + backlog_id + "'");
		}

		return projectTask;
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {

		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		projectTask = updatedTask;

		return projectTaskRepository.save(projectTask);

	}

	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
//		
//		Backlog backlog = projectTask.getBacklog();
//		List<ProjectTask> pts = backlog.getProjectTasks();
//		pts.remove(projectTask);
//		backlogRepository.save(backlog);
		
		projectTaskRepository.delete(projectTask);
		

	}

}
