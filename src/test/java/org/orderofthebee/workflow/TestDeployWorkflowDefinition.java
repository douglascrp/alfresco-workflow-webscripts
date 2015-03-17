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

public class TestDeployWorkflowDefinition {

	private static final String HELLO_WORLD_WORKFLOW_DEF = "activiti$helloWorld";
	private static final String HELLO_WORLD_WORKFLOW_PATH = "alfresco/extension/workflows/helloWorld.bpmn";

	String baseUrl = TestMeta.BASE_URL;

	private String helloWorldWorkflowId;
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
		helloWorldWorkflowId = getHelloWorldWorkflowId();
		
		if (helloWorldWorkflowId != null) {
			String url = baseUrl + "bees-api/workflow-undeploy-definition?workflowDefinitionId=" + helloWorldWorkflowId;
			helper.post(url);
		}
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
	public void testDeployHelloWorldWorkflow() throws IOException,
			JSONException {

		String url = baseUrl + "bees-api/workflow-deploy-definition?workflowDefinitionPath=" + HELLO_WORLD_WORKFLOW_PATH;
		helper.post(url);
		
		assertNotNull(HELLO_WORLD_WORKFLOW_DEF + " not deployed", getHelloWorldWorkflowId());
	}

}