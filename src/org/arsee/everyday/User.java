package org.arsee.everyday;

public class User {
	public static User getInstance(){
		if(m_myself == null){
			m_myself = new User();
		}
		
		return m_myself;
	}
	
	private static User m_myself = null;
	
	public String getName(){ return "Kangkang";}
	public int getID(  ){ return 1;}
}
