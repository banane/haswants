/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.w2.haswants;

import java.util.Locale;

public class Constants  {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This sample App is for demonstration purposes only.
    // It is not secure to embed your credentials into source code.
    // Please read the following article for getting credentials
    // to devices securely.
    // http://aws.amazon.com/articles/Mobile/4611615499399490
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	public static final String ACCESS_KEY_ID = "AKIAI6P3ZBS52HYSAJEA";
	public static final String SECRET_KEY = "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";
	
	
	public static final String PICTURE_BUCKET = "mobileprofiles";
	public static final String BASE_URL = "http://ec2-54-215-227-19.us-west-1.compute.amazonaws.com:3000";
//	public static final String BASE_URL = "http://10.0.1.155:3000";
//	public static final String PICTURE_NAME = "profile_" + person.getMyId() + "_"+ numRandom.toString() +".png";
	
	
	public static String getPictureBucket() {
		return ("my-unique-name" + ACCESS_KEY_ID + PICTURE_BUCKET).toLowerCase(Locale.US);
	}
	
}
