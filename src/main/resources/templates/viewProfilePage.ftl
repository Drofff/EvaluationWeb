<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/navbar.ftl">

<#if error??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error}</p>
    </div>
</#if>

<div class="ui segment">
    <#if profileData??>
        <div class="ui segment materials">
            <h1 class="ui header">Materials</h1>
            <div class="ui divider"></div>
            <#if nodes??>
                <table class="ui celled striped table" style=" width: 100%;">
                    <thead>
                    <tr>
                        <th colspan="2">
                            <#if path??>
                                <div class="ui breadcrumb">
                                    <a class="section" href="${root}">root</a>
                                    <div class="divider"> / </div>
                                    <#list path as segment, url>
                                        <a class="section" href="${url}">${segment}</a>
                                        <div class="divider"> / </div>
                                    </#list>
                                    <div class="active section">${currentDirectory}</div>
                                </div>
                            <#else>
                                root
                            </#if>
                        </th>
                    </tr></thead>
                    <tbody>
                    <#list nodes as node>
                        <tr>
                            <td>
                                <#if node.dir>
                                    <i class="folder icon"></i>
                                    <a href="${currentUrl}/${node.name}">${node.name}</a>
                                <#else>
                                    <i class="file outline icon"></i>
                                    <a href="/storage/download/${node.id}">${node.name}</a>
                                </#if>
                            </td>
                            <td style="text-align: center">
                                ${node.size}
                            </td>
                        </tr>
                    </#list>
                    <#if nodes?? && nodes?size gt 0> <#else>
                        <h4 class="header">You have not any files yet</h4>
                    </#if>
                    </tbody>
                </table>
                <br>

            </#if>
        </div>
        <div>
            <img class="ui medium circular image" src="${profileData.photoUrl}" style="left: 7%;  width: 250px; height: 250px; margin-top: 10px;">
            <div style="text-align : center; margin-top: 2%;">
                <h1 style="left: 7%; width: 30%">${profileData.firstName} ${profileData.lastName}</h1>
            </div>
            <h4 style="margin-left: 7%;"><i class="address card icon"></i> Position: ${profileData.position}</h4>
            <h4 style="margin-left: 7%;"><i class="users icon"></i> Group: ${profileData.groupName}</h4>
	        <h4 style="margin-left: 7%;">
		        <a class="ui primary button" href="/mail/send?receiverId=${user_id}">
		            Send message
	            </a>
	        </h4>
            <div class="ui segment groups">
                <h1 class="ui header">Groups</h1>
                <div class="ui divider"></div>
                <#if profileData.studentsGroups??>
	            <div class="ui list">
                    <#list profileData.studentsGroups as studentGroup>
	                    <div class="item">
                            ${studentGroup}
	                    </div>
                    </#list>
	            </div>
                </#if>
            </div>
            <div class="ui segment subject">
                <h1 class="ui header">Subjects</h1>
                <div class="ui divider"></div>
                <#if profileData.subjects??>
		            <div class="ui list">
                        <#list profileData.subjects as subject>
				            <div class="item">
                                ${subject}
				            </div>
                        </#list>
		            </div>
                </#if>
            </div>

        </div>
    </#if>
</div>



</body>
</html>