package org.orderofthebee.repo.web.scripts.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class WorkflowDeletePost extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_ID = "workflowId";

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		// copy some stuff from alfresco WorkflowUndeployDefinitionGet

		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		// Get the definition id from the params
		String workflowId = params.get(PARAM_WORKFLOW_ID);
//		System.out.println("workflowId = " + workflowId);

		WorkflowInstance workflow = workflowService
				.getWorkflowById(workflowId);

		// Workflow definition is not found, 404
		if (workflow == null) {
			throw new WebScriptException(HttpServletResponse.SC_NOT_FOUND,
					"Unable to find workflow with id: " + workflowId);
		}

		workflowService.deleteWorkflow(workflowId);

		
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "Workflow  " + workflowId
				+ " deleted");
		return model;

	}

}
