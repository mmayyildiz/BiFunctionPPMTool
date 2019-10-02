package com.bifunction.ppmtool.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

// defines an entity object so that we can create a table with attributes
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
	@Column(updatable=false)
	private Date created_Date;
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date updated_Date;
	
	// When we load a project object then the back information is available
	// CascadeType.ALL : the project is the owner side of relationship
	// meaning that if I delete the project then everything that's a child to the project should be deleted
	@OneToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "project")
	@JsonIgnore  // I don't need to get backlog there
	private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;


 private String projectLeader;
	
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
	

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}
	
    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
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
