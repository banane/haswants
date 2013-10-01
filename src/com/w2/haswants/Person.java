package com.w2.haswants;

/*   This object is important to store the user's ID, so we can 
 *   access the correct "edit profile" and we can store the correct
 *   image on S3 for them.
 */

public class Person {
	
	private String FirstName;
	private String Id;
	
	public Person(String first_name, String id){
		this.FirstName = first_name;
		this.Id = id;
	}
	
	public String getFirstname(){
		return FirstName;
	}
	
	public String getId(){
		return Id;
	}

}
