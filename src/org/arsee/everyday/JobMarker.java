package org.arsee.everyday;

public class JobMarker {
	private Job job;
	private Day day;
	private TimeArea area;	
	boolean isMaked = false;
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public TimeArea getArea() {
		return area;
	}
	public void setArea(TimeArea area) {
		this.area = area;
	}
	public boolean isMaked() {
		return isMaked;
	}
	public void setMaked(boolean isMaked) {
		this.isMaked = isMaked;
	}
	
}
