{	
	"data": [
<#if workflows?has_content>
<#list workflows as w>
		{
			"id": "${w.id}",		
			"active": ${w.active?c},		
			"context": "${w.context!""}",		
			"definition": "${w.definition}",		
			"description": "${w.description!""}",		
			"dueDate": "${w.dueDate!""}",		
			"endDate": "${w.endDate!""}",		
			"initiator": "${w.initiator}",		
			"priority": "${w.priority!""}",		
			"startDate": "${w.startDate!""}",		
			"workflowPackage": "${w.workflowPackage!""}"		
		}<#if w_has_next>,</#if>
</#list>
</#if>
	]
}