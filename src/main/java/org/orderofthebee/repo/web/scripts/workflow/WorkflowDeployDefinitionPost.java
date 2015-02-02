package org.orderofthebee.repo.web.scripts.workflow;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.workflow.WorkflowDeployment;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Takes a string representation of a NodeRef and deploys the workflow definition found there
 * TODO needs a bunch of error checking
 * @author martian
 *
 */
public class WorkflowDeployDefinitionPost extends AbstractWorkflowWebscript {
	private static final Object PARAM_WORKFLOW_DEFINITION = "workflowDefinition";

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		String workflowDefinition = params.get(PARAM_WORKFLOW_DEFINITION);

		WorkflowDeployment deployed = workflowService.deployDefinition(new NodeRef(workflowDefinition));

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "Deployed definition  " + workflowDefinition
				+ " deleted");
		model.put("workflowDefinitionId", deployed.getDefinition().getId());

		return model;

	}

}
