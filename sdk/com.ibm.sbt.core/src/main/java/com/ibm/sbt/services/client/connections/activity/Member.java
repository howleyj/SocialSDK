/*
 * � Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections.activity;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
import com.ibm.sbt.services.client.connections.activity.transformers.ActivityMemberTransformer;

/**
 * This class represents a Connections Activity Member entity
 * 
 * @Represents Connections Activity Member
 * @author Vimal Dhupar
 */
public class Member extends BaseEntity {
	
	/**
     * The id of the Member Entry
     */
    private String id;

	/**
	 * Constructor
	 * 
	 * @param activityService
	 * @param id
	 */
	public Member(ActivityService activityService, String id) {
		setService(activityService);
		if(id.contains("@")){
			setEmail(id);
		}
		else{
			setUserid(id);
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param svc
	 * @param handler
	 */
	public Member(ActivityService svc, DataHandler<?> handler){
		super(svc,handler);
	}

	public String getActivityId(){
	    	String activityId = "";
	    	try {
	    		activityId = getAsString(ActivityXPath.Id);
			} catch (Exception e) {}
	    	
	    	if(StringUtil.isEmpty(activityId)){
	    		id = activityId;
	    	}
	    	id = id.substring(id.indexOf("=")+1, id.indexOf("&"));
	    	return id;
	}
	
	/**
	 * @return id
	 */
	public String getUserid() {
		return getAsString(ActivityXPath.ContributorUserUuid);
	}
	
	/**
	 * @return id
	 */
	public String getMemberId() {
		String memberId =  getAsString(ActivityXPath.MemberId);
		int start = memberId.lastIndexOf("memberid=");
		memberId = memberId.substring(start);
		int endAmp = memberId.indexOf("&");
		if(endAmp == -1)
			return memberId.substring("memberid=".length());
		else 
			return memberId.substring("memberid=".length(), endAmp);
	}
	
	/**
	 * @return name
	 */
	public String getName() {
		return getAsString(ActivityXPath.ContributorName);
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return getAsString(ActivityXPath.ContributorEmail);
	}
	
	public String getState() {
		return getAsString(ActivityXPath.ContributorUserState);
	}
	
	public String getTitle() {
		return getAsString(ActivityXPath.Title);
	}
	
	public String getUpdated() {
		return getAsString(ActivityXPath.Updated);
	}
	
	public String getSummary() {
		return getAsString(ActivityXPath.summary);
	}
	
	public String getCategory() {
		return getAsString(ActivityXPath.Category);
	}
	
	public String getPermissions() {
		return getAsString(ActivityXPath.Permissions);
	}
	
	/**
	 * @return role
	 */
	public String getRole() {
		return getAsString(ActivityXPath.role);
	}
	
	/**
	 * @set name
	 */
	public void setName(String name) {
		setAsString(ActivityXPath.ContributorName, name);
	}

	/**
	 * @set id
	 */
	public void setUserid(String id) {
		setAsString(ActivityXPath.ContributorUserUuid, id);
	}
	
	/**
	 * @set email
	 */
	public void setEmail(String email) {
		setAsString(ActivityXPath.ContributorEmail, email);
	}
	
	public void setCategory(String category) {
		setAsString(ActivityXPath.Category, category);
	}
	
	/**
	 * @set role
	 */
	public void setRole(String role){
		setAsString(ActivityXPath.role, role);
	}
		
	public Member load() throws ActivityServiceException
	{
	  	return getService().getMember(getActivityId(), getUserid());
	}
	 
	public void remove() throws ActivityServiceException
	{
	  	getService().deleteMember(getActivityId(), getUserid());
	}
	
	@Override
	public ActivityService getService(){
		return (ActivityService)super.getService();
	}
	
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)super.getDataHandler();
	}

	public Object constructPayload() throws TransformerException {
		ActivityMemberTransformer memTrans = new ActivityMemberTransformer();
		return convertToXML(memTrans.transform(fields));
	}

	/**
     * convertToXML
     * <p>
     * Utility method to construct XML payload
     * 
     * @param requestBody
     * @return Document
     */
    public Document convertToXML(String requestBody) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(requestBody.toString())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
