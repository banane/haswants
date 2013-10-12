package com.w2.haswants;

import java.io.Serializable;

/*   This object is important to store the user's ID, so we can 
 *   access the correct "edit profile" and we can store the correct
 *   image on S3 for them.
 */

public class Person implements Serializable {
	
	private String FirstName;
	private String MyId;
	private String ProfilePhoto;
	private String AuthToken;
	
	public Person(String first_name, String my_id, String profile_photo, String auth_token){
		this.FirstName = first_name;
		this.MyId = my_id;
		this.ProfilePhoto = profile_photo;
		this.AuthToken = auth_token;
	}
	
	public String getFirstName(){
		return FirstName;
	}
	
	public String getAuthToken(){
		return AuthToken;
	}

	public String getMyId(){
		return MyId;
	}
	
	public String getProfilePhoto(){
		return ProfilePhoto;
	}

}
