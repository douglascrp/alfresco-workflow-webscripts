package org.orderofthebee.workflow;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStartWorkflow {

	private static final String ADHOC_WF_NAME = "activiti$activitiAdhoc";
	
	private static WebScriptHelper helper;
	static String baseUrl = TestMeta.BASE_URL;

	private static String wfdid;
	
	@BeforeClass
	public static void setupClass() throws IOException, JSONException{
		helper = new WebScriptHelper(TestMeta.AUTH_USER, TestMeta.AUTH_PASSWORD);
		JSONObject json = helper.readJsonFromUrl(baseUrl + "bees-api/list-workflow-definitions");

		JSONArray data = (JSONArray) json.get("data");
		
		wfdid = null;
		for(int i=0; i < data.length(); i++){
			JSONObject item = data.getJSONObject(i);
			if(item.getString("name").equals(ADHOC_WF_NAME)){
				wfdid = item.getString("id");
				break;
			}
		}
		assertNotNull("Finding id failed for wf def name: " + ADHOC_WF_NAME, wfdid);
	}
	
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testStartWorkflow() throws Exception{
		String url = baseUrl + "bees-api/workflow-start?workflowDefinitionId=" + wfdid;
		url += "&bpmAssigneeName=" + TestMeta.START_WF_USER;
		
		InputStream is = helper.post(url);
		
		if(is==null){
			throw new Exception("Null inputstream returned from POST to " + url);
		}
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));
		String result = helper.readAll(rd);
		System.out.println(result);
	}
	
}
