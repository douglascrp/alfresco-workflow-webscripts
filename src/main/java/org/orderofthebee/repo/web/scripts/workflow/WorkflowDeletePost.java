package org.orderofthebee.repo.web.scripts.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class WorkflowDeletePost extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_ID = "workflowId";

	private static Logger log = Logger.getLogger(WorkflowDeletePost.class);

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		log.debug("starting buildModel");
		log.debug(req.toString());
	
		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		// Get the definition id from the params
		String workflowId = req.getParameter(PARAM_WORKFLOW_ID);
		
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

		log.debug("finish buildModel");
		
		return model;

	}

}
