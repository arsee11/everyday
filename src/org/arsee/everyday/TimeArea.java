//TimeArea.java

package org.arsee.everyday;

import java.util.ArrayList;

//(being, end] area
public class TimeArea{

	public TimeArea(int begin, int end){
		this.begin = new Clock(begin);
		this.end = new Clock(end);
	}
	
	
	public Clock getBegin() {
		return begin;
	}
	
	
	public Clock getEnd() {
		return end;
	}
	
	
	public ArrayList<Job> getJobs() {
		return jobs;
	}
	
	
	public void attachJob(Job job){
		jobs.add(job);
	}
	
	public boolean isIn(int clock){
		return (clock >= begin.get() && clock <end.get());
	}
	
	public boolean isLessThan(int clock){
		return (clock <  begin.get());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public  ArrayList<Integer> split(){
		ArrayList<Integer> clocks = new ArrayList<Integer>();
		for(int i=begin.get(); i<= end.get(); i++){
			clocks.add(i);
		}
		
		return clocks;
	}
	private int  id;		
	private Clock begin;
	private Clock end;
	
	ArrayList<Job> jobs = new ArrayList<Job>();
}
