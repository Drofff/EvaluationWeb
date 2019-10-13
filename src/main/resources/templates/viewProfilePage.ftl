<html>
<head>
    <title>Evaluation Web</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
<body>
<div class="ui menu">
    <div class="header item">
        Evaluation Web
    </div>
    <a class="item" href="/">
        Schedule
    </a>
    <a class="item" href="/test">
        Tests
    </a>
    <a class="item" href="/profile">
        Profile
    </a>
    <#if isTeacher?? && isTeacher>
    <a class="item" href="/teacher/manager">
        Lessons Manager
    </a>
    <a class="item" href="/teacher/storage">
        My Storage
    </a>
</#if>
<#if isAdmin?? && isAdmin>
<a class="item" href="/admin/users">
    Admin Panel
</a>
</#if>
<div class="right menu">
    <div class="item">
        <a class="ui primary button" href="/logout">Log out</a>
    </div>
</div>
</div>

<#if error??>
<div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
    <p>${error}</p>
</div>
</#if>

<div class="ui segment">
    <#if profileData??>
    <div>
        <img class="ui medium circular image" src="${profileData.photoUrl}" style="margin-left: auto; margin-right: auto; width: 250px; height: 250px; margin-top: 10px;">
        <div style="text-align : center; margin-top: 2%;">
            <h1 style="margin-left: auto; margin-right: auto; width:30%">${profileData.firstName} ${profileData.lastName}</h1>
        </div>
        <h4 style="margin-left: 35%;"><i class="address card icon"></i> Position: ${profileData.position}</h4>
        <h4 style="margin-left: 35%;"><i class="users icon"></i> Group: ${profileData.groupName}</h4>
        <h4 style="margin-left: 35%;"><i class="users icon"></i>Email: ${profileData.email}</h4>
        <h4 style="margin-left: 35%;">
            <#if profileData.subjects??>
                <#list profileData.subjects as subject>
                    ${subject}
                </#list>
            </#if>
            <#if profileData.studentsGroups??>
                <#list profileData.studentsGroups as studentGroup>
                    ${studentGroup}
                </#list>
            </#if>
        </h4>
    </div>
</#if>
</div>

<#if nodes??>
    <table class="ui celled striped table" style="margin-left: 40%; width: 40%;">
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

</#if>

</body>

</html>