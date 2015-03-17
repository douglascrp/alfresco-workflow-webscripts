package org.orderofthebee.repo.web.scripts.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
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

	private static Logger log = Logger.getLogger(ListActiveWorkflowsGet.class);
	
	
	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {
		
		log.debug("starting buildModel");
		log.debug(req.toString());

		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		String workflowDefinitionId = params.get(PARAM_WORKFLOW_DEFINITION_ID);

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
			out.put("dueDate", formatDate(workflow.getDueDate()));
			out.put("endDate", formatDate(workflow.getEndDate()));
			out.put("id", workflow.getId());
			out.put("initiator", workflow.getInitiator().toString());
			out.put("priority", workflow.getPriority());
			out.put("startDate", formatDate(workflow.getStartDate()));
			out.put("workflowPackage", workflow.getWorkflowPackage());
			
			workflowsOut.add(out);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("workflows", workflowsOut);

		log.debug("finish buildModel");
		
		return model;

	}

	private String formatDate(Date date) {
		if (date==null) return null;
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.print(date.getTime());
	}
	

}
