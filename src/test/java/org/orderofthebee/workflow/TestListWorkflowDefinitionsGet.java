package org.orderofthebee.workflow;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestListWorkflowDefinitionsGet {

	
	private static final String[] UNAVAILABLE_WORKFLOW_DEFS = { "activiti$notRealWorkflow",
		"onlyNeedOneOrTwoToProve",
	};
	private static final String[] AVAILABLE_WORKFLOW_DEFS = { "activiti$activitiInvitationNominated",
		"activiti$activitiParallelGroupReview",
		"activiti$activitiParallelReview", 
		"activiti$activitiReviewPooled",
		"activiti$publishWebContent",
		"activiti$activitiAdhoc",
		"activiti$activitiInvitationModerated",
		"activiti$activitiReview"
	};
	String baseUrl = TestMeta.BASE_URL;
	private static WebScriptHelper helper;
	private static JSONArray data;

	@BeforeClass
	public static void setupClass() {
		helper = new WebScriptHelper(TestMeta.AUTH_USER, TestMeta.AUTH_PASSWORD);
	}

	@Before
	public void setupTest() throws IOException, JSONException{
		JSONObject json = helper.readJsonFromUrl(baseUrl + "bees-api/list-workflow-definitions");

		data = (JSONArray) json.get("data");

	}
	
	@Test
	public void testListDefsIsArrayGreaterZero() {
		
		assertTrue(data.length() > 0);

	}

	@Test
	public void testListDefsContainsExpected() throws IOException,
			JSONException {

		String[] names = AVAILABLE_WORKFLOW_DEFS;

		for(String name: names){
			boolean found = false;
			for(int i=0; i < data.length(); i++){
				JSONObject item = (JSONObject) data.get(i);
				if(name.equals(item.getString("name"))){
					found = true;
					break;
				}
			}
			assertTrue("Found " + name, found);
		}
	
	}

	
	@Test
	public void testListDefsNotContainingUnexpected() throws IOException, JSONException{

		String[] names = UNAVAILABLE_WORKFLOW_DEFS;

		for(String name: names){
			boolean found = false;
			for(int i=0; i < data.length(); i++){
				JSONObject item = (JSONObject) data.get(i);
				if(name.equals(item.getString("name"))){
					found = true;
					break;
				}
			}
			assertFalse("Found " + name, found);
		}
	}
	
}

