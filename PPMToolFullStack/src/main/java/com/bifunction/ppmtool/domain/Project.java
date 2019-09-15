package com.bifunction.ppmtool.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

// defines an entity object sho that we can create a table with attributes
@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="Project name is required")
	private String projectName;
	@NotBlank(message="Project identifier is required")
	@Size(min=4, max=5, message="Please use 4 to 5 characters")
	// db level validation
	@Column(updatable=false, unique=true)
	private String projectIdentifier;
	@NotBlank(message="Project description is required")
	private String description;
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date start_date;
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date end_date;
	
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date created_Date;
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date updated_Date;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}

	public Date getUpdated_Date() {
		return updated_Date;
	}

	public void setUpdated_Date(Date updated_Date) {
		this.updated_Date = updated_Date;
	}

	// every time we created a new object 
	@PrePersist
	protected void onCreate(){
		this.created_Date =  new Date();
	}
	
	// every time we update the object 
	@PreUpdate
	protected void onUpdate() {
	    this.updated_Date = new Date();
	}
	
	
}
