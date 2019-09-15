package com.bifunction.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bifunction.ppmtool.domain.Project;

// this annotation is no longer necessary
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	// CrudRepository gives us a set of methods 

    Project findByProjectIdentifier(String projectIdentifier);
	
	@Override
	Iterable<Project> findAll();

}
