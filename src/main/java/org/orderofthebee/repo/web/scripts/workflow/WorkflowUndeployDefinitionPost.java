package org.orderofthebee.repo.web.scripts.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;


public class WorkflowUndeployDefinitionPost extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_DEFINITION_ID = "workflowDefinitionId";

	private static Logger log = Logger.getLogger(WorkflowUndeployDefinitionPost.class);

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {
		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		log.debug("starting buildModel");
		log.debug(req.toString());

		// Get the definition id from the params
		String workflowDefinitionId = req.getParameter(PARAM_WORKFLOW_DEFINITION_ID);
		
		System.out.println("workflowDefinitionId = " + workflowDefinitionId);

		WorkflowDefinition workflowDefinition = workflowService
				.getDefinitionById(workflowDefinitionId);

		// Workflow definition is not found, 404
		if (workflowDefinition == null) {
			throw new WebScriptException(HttpServletResponse.SC_NOT_FOUND,
					"Unable to find workflow definition with id: "
							+ workflowDefinitionId);
		}

		workflowService.undeployDefinition(workflowDefinitionId);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "Workflow definition " + workflowDefinition
				+ " undeployed");

		log.debug("finish buildModel");
		
		return model;
	}
}