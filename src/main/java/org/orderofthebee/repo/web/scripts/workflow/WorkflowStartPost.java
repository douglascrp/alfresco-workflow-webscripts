package org.orderofthebee.repo.web.scripts.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.service.cmr.workflow.WorkflowPath;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class WorkflowStartPost extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_DEFINITION_ID = "workflowDefinitionId";

	private Logger log = Logger.getLogger(WorkflowStartPost.class);
	
	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		// copy some stuff from alfresco WorkflowUndeployDefinitionGet

		log.info("Starting buildmodel");
		
		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		for(String key: params.keySet()){
			log.info("key=" + key + ", val=" + params.get(key));
		}
		
		// Get the definition id from the params
		String workflowDefinitionId = params.get(PARAM_WORKFLOW_DEFINITION_ID);

		log.info("workflow definition id = " + workflowDefinitionId);
		
		Map<QName, Serializable> wparams = new HashMap<QName, Serializable>();
		
		// TODO no idea what to pass for wparams
		
		WorkflowPath wf = workflowService.startWorkflow(workflowDefinitionId, wparams);
		
		log.info("workflow id" + wf.getId());
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		
		model.put("id", wf.getId());
		model.put("instance", wf.getInstance());
		model.put("node", wf.getNode());

		
		return model;

	}

}
