package org.orderofthebee.repo.web.scripts.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.workflow.WorkflowPath;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class WorkflowStartPost extends AbstractWorkflowWebscript {
	private static final String PARAM_WORKFLOW_DEFINITION_ID = "workflowDefinitionId";

	private static final String PARAM_BPM_ASSIGNEE_NAME = "bpmAssigneeName";

	private Logger log = Logger.getLogger(WorkflowStartPost.class);
	
	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {

		// copy some stuff from alfresco WorkflowUndeployDefinitionGet

		log.warn("Starting buildmodel");
		log.warn(req.getServiceMatch().getTemplate());
		Map<String, String> params = req.getServiceMatch().getTemplateVars();

		log.warn(req.getContextPath());
		for(String paramName: req.getParameterNames()){
			log.warn("param name " + paramName);
		}
		
		
		
		for(String key: params.keySet()){
			log.warn("key=" + key + ", val=" + params.get(key));
		}
		
		// Get the definition id from the params
		String workflowDefinitionId = req.getParameter(PARAM_WORKFLOW_DEFINITION_ID);
		String bpmAssigneeName = req.getParameter(PARAM_BPM_ASSIGNEE_NAME);
		
		
		log.warn("workflow definition id = " + workflowDefinitionId);
		
		Map<QName, Serializable> workflowProps = new HashMap<QName, Serializable>();
		
		NodeRef person = personService.getPerson(bpmAssigneeName);

		workflowProps.put(WorkflowModel.ASSOC_ASSIGNEE, person);
		
		WorkflowPath wf = workflowService.startWorkflow(workflowDefinitionId, workflowProps);
		
		log.warn("workflow id" + wf.getId());
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		
		model.put("id", wf.getId());
		model.put("instance", wf.getInstance());
		model.put("node", wf.getNode());

		
		return model;

	}

}
