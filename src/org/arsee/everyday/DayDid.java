//DayDid.java


package org.arsee.everyday;

import java.util.ArrayList;

public class DayDid{

	String date;		 //yyyy-mm-dd
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Element> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Element> jobs) {
		this.jobs = jobs;
	}
	
	public void putJob(Job job){
		Element e = new Element();
		e.time = Day.getNow();
		e.job = job;
		jobs.add(e);
	}
	
	class Element{
		String time;		//hh:mm:ss
		Job job;
	}
	
	ArrayList<Element> jobs=new ArrayList<Element>();;
	
}
