package org.orderofthebee.workflow;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orderofthebee.workflow.meta.TestMeta;

public class TestStartListDeleteWorkflow {

	private static Logger log = Logger.getLogger(TestStartListDeleteWorkflow.class);
	
	private static final String ADHOC_WF_NAME = "activiti$activitiAdhoc";
	
	private static WebScriptHelper helper;
	static String baseUrl = TestMeta.BASE_URL;

	private static String wfdid;
	
	@BeforeClass
	public static void setupClass() throws IOException, JSONException{
		
		log.warn("setupClass");

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
		
		
		log.warn("setupClass done");

	}

	private String activeWorkflowId;
	
	@Before
	public void setup() throws Exception{
		
		log.warn("setup");
		
		String url = baseUrl + "bees-api/workflow-start?workflowDefinitionId=" + wfdid;
		url += "&bpmAssigneeName=" + TestMeta.START_WF_USER;
		
		InputStream is = helper.post(url);
		
		if(is==null){
			throw new Exception("Null inputstream returned from POST to " + url);
		}
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));

		JSONObject res = helper.readJsonFromInputStream(is);
		assertNotNull(res);
		assertNotNull(res.getString("id"));
		assertNotEquals((res.getString("id")), "");
		
		activeWorkflowId = res.getString("id");

	
		log.warn("setup done");

	}
	
	@After
	public void tearDown() throws IOException{
		
		log.warn("tearDown");
		
		String url = baseUrl + "bees-api/workflow-delete?workflowId=" + activeWorkflowId;
		helper.post(url);
		log.warn("tearDown done");

	}
	
	@Test
	public void testListWorkflowOnce() throws Exception {
		doListTest();
	}
	@Test
	public void testListWorkflowTwice() throws Exception {
		doListTest();
	}
	@Test
	public void testListWorkflowThrice() throws Exception {
		doListTest();
	}
	
	public void doListTest() throws Exception {
		log.warn("doListTest");
		String url = baseUrl + "bees-api/list-active-workflows?workflowDefinitionId=" + wfdid;
		JSONObject res = helper.readJsonFromUrl(url);
		assertNotNull(res);
		assertNotNull(res.getJSONArray("data"));
		JSONArray data = res.getJSONArray("data");
		
		assertEquals(data.length(), 1);
		
		JSONObject item = data.getJSONObject(0);
		
		assertEquals(activeWorkflowId, item.get("id"));
		
		log.warn("doListTest done");

	}
	
}
