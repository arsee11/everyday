package org.arsee.everyday;

import java.util.ArrayList;

public class JobType {

	private static class TypeInitilizer extends ArrayList<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TypeInitilizer(){
			add("生活");
			add("工作");
			add("休闲娱乐");
			add("学习");
		}
	}
	public static ArrayList<String> Types = new ArrayList<String>( new JobType.TypeInitilizer());
	public JobType(String type){
		this.type = type;
	}
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	int id;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	String type;
	
	
}
