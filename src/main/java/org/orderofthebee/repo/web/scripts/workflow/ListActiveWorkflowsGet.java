package org.orderofthebee.repo.web.scripts.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowDeployment;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * List all active workflows, or just those matching a given workflowDefinitionId if one is 
 * passed in
 * TODO needs a bunch of error checking
 * @author martian
 *
 */
public class ListActiveWorkflowsGet extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_DEFINITION_ID = "workflowDefinitionId";

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		String workflowDefinitionId = params.get(PARAM_WORKFLOW_DEFINITION_ID);

//		WorkflowDeployment deployed = workflowService.deployDefinition(new NodeRef(workflowDefinitionId));

		List<WorkflowInstance> workflows;
		if(null!=workflowDefinitionId){
			workflows = workflowService.getActiveWorkflows(workflowDefinitionId);
			
		} else {
			workflows = workflowService.getActiveWorkflows();
			
		}
		
		List<Map<String, Object>> workflowsOut = new ArrayList<Map<String, Object>>();
		for(WorkflowInstance workflow : workflows){
			HashMap<String, Object> out = new HashMap<String, Object>();
			out.put("active", workflow.isActive());
			out.put("context", workflow.getContext());
			out.put("definition", workflow.getDefinition());
			out.put("description", workflow.getDescription());
			out.put("dueDate", workflow.getDueDate());
			out.put("endDate", workflow.getEndDate());
			out.put("id", workflow.getId());
			out.put("initiator", workflow.getInitiator());
			out.put("priority", workflow.getPriority());
			out.put("startDate", workflow.getStartDate());
			out.put("workflowPackage", workflow.getWorkflowPackage());
			
			workflowsOut.add(out);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("workflows", workflowsOut);
		return model;

	}

}
