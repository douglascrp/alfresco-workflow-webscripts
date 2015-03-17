package org.orderofthebee.workflow;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orderofthebee.workflow.meta.TestMeta;
import org.orderofthebee.workflow.meta.WebScriptHelper;

public class TestUndeployWorkflowDefinition {

	private static final String HELLO_WORLD_WORKFLOW_DEF = "activiti$helloWorld";

	String baseUrl = TestMeta.BASE_URL;

	private String hellowWorldWorkflowId;
	private static WebScriptHelper helper;
	private static JSONArray data;

	@BeforeClass
	public static void setupClass() {
		helper = new WebScriptHelper(TestMeta.AUTH_USER, TestMeta.AUTH_PASSWORD);
	}

	@Before
	public void setupTest() throws IOException, JSONException {
		JSONObject json = helper.readJsonFromUrl(baseUrl
				+ "bees-api/list-workflow-definitions");

		data = (JSONArray) json.get("data");
		hellowWorldWorkflowId = getHelloWorldWorkflowId();
	}

	public String getHelloWorldWorkflowId() throws IOException,
			JSONException {

		JSONObject json = helper.readJsonFromUrl(baseUrl
				+ "bees-api/list-workflow-definitions");

		data = (JSONArray) json.get("data");
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject item = (JSONObject) data.get(i);
			if (HELLO_WORLD_WORKFLOW_DEF.equals(item.getString("name"))) {
				return item.getString("id");
			}
		}
		return null;
	}
	
	@Test
	public void testUndeployHelloWorldWorkflow() throws IOException,
			JSONException {

		String url = baseUrl + "bees-api/workflow-undeploy-definition?workflowDefinitionId=" + hellowWorldWorkflowId;
		helper.post(url);
		
		assertNull(HELLO_WORLD_WORKFLOW_DEF + " undeployied", getHelloWorldWorkflowId());
	}

}