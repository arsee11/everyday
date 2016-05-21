package org.arsee.everyday;

import java.util.ArrayList;

public class JobClass {
	public JobClass(String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String name;
	
	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void addJob(Job job){
		jobs.add(job);
	}
	
	private ArrayList<Job> jobs;
}
