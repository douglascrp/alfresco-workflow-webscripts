package org.orderofthebee.repo.web.scripts.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.web.scripts.workflow.AbstractWorkflowWebscript;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.repo.workflow.activiti.ActivitiConstants;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowDeployment;
import org.apache.log4j.Logger;
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
	private static final String PARAM_WORKFLOW_DEFINITION_NODE_REF = "workflowDefinitionNodeRef";
	private static final String PARAM_WORKFLOW_DEFINITION_PATH = "workflowDefinitionPath";
	private static final String XML = MimetypeMap.MIMETYPE_XML;

	private static Logger log = Logger.getLogger(WorkflowDeployDefinitionPost.class);

	@Override
	protected Map<String, Object> buildModel(WorkflowModelBuilder modelBuilder,
			WebScriptRequest req, Status status, Cache cache) {
		log.debug("starting buildModel");
		log.debug(req.toString());

		Map<String, String> params = req.getServiceMatch().getTemplateVars();
		
		String workflowDefinition = null;
		WorkflowDefinition deployed = null;
		
		if (req.getParameter(PARAM_WORKFLOW_DEFINITION_NODE_REF) != null) {
			workflowDefinition = req.getParameter(PARAM_WORKFLOW_DEFINITION_NODE_REF);
			System.out.println("workflowDefinitionNodeRef = " + workflowDefinition);
			deployed = deployDefinition(new NodeRef(workflowDefinition));
		} else {
			workflowDefinition = req.getParameter(PARAM_WORKFLOW_DEFINITION_PATH);
			System.out.println("workflowDefinitionPath = " + workflowDefinition);
			deployed = deployDefinition(workflowDefinition);
		}
		
		System.out.println("workflowDefinition = " + workflowDefinition);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "Deployed definition  " + workflowDefinition);
		model.put("workflowDefinitionId", deployed.getId());

		log.debug("finish buildModel");
		
		return model;
	}

	protected WorkflowDefinition deployDefinition(NodeRef nodeRef)
    {
        WorkflowDeployment deployment = workflowService.deployDefinition(nodeRef);
        WorkflowDefinition definition = deployment.getDefinition();
        return definition;
    }
	
	protected WorkflowDefinition deployDefinition(String resource)
    {
        InputStream input = getInputStream(resource);
        //TODO: Are we going to keep JBPM as an option or is it ok to keep only Activiti?
        WorkflowDeployment deployment = workflowService.deployDefinition(ActivitiConstants.ENGINE_ID, input, XML);
        WorkflowDefinition definition = deployment.getDefinition();
        return definition;
    }
	
    private InputStream getInputStream(String resource)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resource);
    }
}