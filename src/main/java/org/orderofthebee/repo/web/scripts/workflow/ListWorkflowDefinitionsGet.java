package org.orderofthebee.repo.web.scripts.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.apache.log4j.Logger;
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
public class ListWorkflowDefinitionsGet extends AbstractWorkflowWebscript {

	private static Logger log = Logger.getLogger(ListWorkflowDefinitionsGet.class);

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

//		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		log.debug("starting buildModel");
		log.debug(req.toString());
	
		List<WorkflowDefinition> defs = workflowService.getAllDefinitions();
		
		
		List<Map<String, Object>> defsOut = new ArrayList<Map<String, Object>>();
		for(WorkflowDefinition wDef : defs){
			HashMap<String, Object> out = new HashMap<String, Object>();

			out.put("description", wDef.getDescription());
			out.put("id", wDef.getId());
			out.put("name", wDef.getName());
			out.put("startTaskDefinition", wDef.getStartTaskDefinition());
			out.put("title", wDef.getTitle());
			out.put("version", wDef.getVersion());
		
			
			defsOut.add(out);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("definitions", defsOut);

		log.debug("finish buildModel");
		
		return model;

	}

}
