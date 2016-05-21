//Day.java

package org.arsee.everyday;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class Day{

	public Day(String date){
		this .date = date;
	}
	
	//@return yyyy-MM-dd
	public static String  getToday(){
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(today);
	}
	
	//@return hh:mm:ss
	public static String  getNow(){
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
		return f.format(today);
	}
	
	public static String FormatDay(long msecond){
		Date d = new Date(msecond);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(d);
	}
	Day(){
		for(int i=0; i<24; i++){
			clocks.add(i);
		}
	}

	public ArrayList<Integer> getClocks() {
		return clocks;
	}

	public ArrayList<TimeArea> getAreas() {
		return areas;
	}
	
	public void addTimeArea(TimeArea value) {
		this.areas.add(value);
	}

	public void split(int begin, int end, Job job){	
		if(check(begin, end) ){
			TimeArea area = new TimeArea(begin, end);
			area.attachJob(job);
			areas.add(area);
		}
	}

	boolean check(int begin, int end){
		return true;
	}

	ArrayList<Integer> clocks = new ArrayList<Integer>();
	ArrayList<TimeArea> areas = new ArrayList<TimeArea>();
	String date;
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
