{
	"data": [
	<#if definitions?has_content>
	<#list definitions as d>
	{
			"description": "${d.description}",
			"id": "${d.id}",
			"name": "${d.name}",
			"startTaskDefinition": "${d.startTaskDefinition}",
			"title": "${d.title}",
			"version": "${d.version}"
	}<#if d_has_next>,</#if>
	</#list>
	</#if>	
]}