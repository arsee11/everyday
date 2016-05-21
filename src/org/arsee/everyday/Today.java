package org.arsee.everyday;

public class Today  {
	private Today(){}
	public static Today instance(){
		return myself;
	}
	private static Today myself = new Today();
	public Day mDay;
	public void  setDay(Day day){
		mDay = day;
	}	
	public Day getDay(){
		return mDay;
	}
}
