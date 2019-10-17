<html>
<head>
    <title>Evaluation Web - My Storage</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <link rel="stylesheet" href="/resources/style.css">

</head>
<body>
<#include "parts/teacherNavbar.ftl">

<#if errorMessage??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${errorMessage}</p>
    </div>
</#if>

<#if message??>
    <div class="ui positive message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${message}</p>
    </div>
</#if>

<div class="ui modal" id="file_upload">
        <i class="close icon"></i>
        <div class="header">
            Upload file
        </div>
        <div class="image content">
            <div class="ui medium image">
                <img src="https://cloud.google.com/images/storage/hero-overview-storage.png?hl=ru">
            </div>
            <div class="description">
                <div class="ui header">Select file</div>
                <div class="ui message">
                    <div class="header">
                        Size limit
                    </div>
                    <p>Please, be aware that maximum file size is 5 MB. If you need more, please, contact administrators</p>
                </div>
                <form action="/teacher/storage/upload" method="post" enctype="multipart/form-data">
                    <input type="file" name="file">
                    <#if fullPath??>
                        <input type="hidden" name="path" value="${fullPath}">
                    </#if>
                    <button class="ui positive right labeled icon button" type="submit">
                        Upload file
                        <i class="cloud upload icon"></i>
                    </button>
                </form>
            </div>
        </div>
        <div class="actions">
            <div class="ui black deny button" id="close-modal">
                Cancel
            </div>
        </div>
</div>

<div class="ui segment grid">

    <p style="position : absolute; margin-top: 15px;">
        <#if permissions??>
        Access is open for: ${permissions}
        <#else>
        Public
        </#if>
    </p>

    <button class="ui primary button" id="create_dir_button" style="margin-top: 10px; margin-left: 55%;">
        Create directory
    </button>

    <button class="ui primary button" style="margin-top: 10px;" id="upload-button">
        Upload file <i class="cloud upload icon" style="margin-left: 5px;"></i>
    </button>
    <#if id??>
        <button class="ui button" id="edit_button" style="margin-top: 10px;">
            Edit permissions
        </button>
    </#if>

    <table class="ui celled striped table" style="width: 100%;">
    <thead>
    <tr>
        <th colspan="4">
        <#if path??>
            <div class="ui breadcrumb">
                <a class="section" href="/teacher/storage">root</a>
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
            <#else>
                <i class="file outline icon"></i>
            </#if>
            ${node.name}
        </td>
        <td style="text-align: center">
            ${node.size}
        </td>
        <td style="text-align: center">
            <#if node.dir>
                <a class="ui primary button" <#if fullPath??> href="/teacher/storage/${fullPath}/${node.name}"
                <#else> href="/teacher/storage/${node.name}" </#if> >
                    Open
                </a>
            <#else>
                <a class="ui primary button" href="/storage/download/${node.id}">
                    Download <i class="cloud download icon" style="margin-left: 5px;"></i>
                </a>
            </#if>
        </td>
        <td class="right aligned" style="text-align: center">
            <#if node.empty>
                <a class="ui red button" href="/teacher/storage/delete/${node.id}">
                    <i class="trash alternate icon"></i> Delete
                </a>
            <#else>
                <button class="ui disabled red button" data-content="Directory is not empty">
                    <i class="trash alternate icon"></i> Delete
                </button>
            </#if>
        </td>
    </tr>
    </#list>
    <#if nodes?? && nodes?size gt 0> <#else>
        <h4 class="header">You have not any files yet</h4>
    </#if>
    </tbody>
</table>

<div class="ui buttons" style="margin-left: 35%; margin-bottom: 3%;">
    <#if prevURL??>
        <a class="ui labeled icon button" href="${prevURL}">
            <i class="left chevron icon"></i>
            Previous
        </a>
    <#else>
        <a class="ui labeled icon button disabled">
            <i class="left chevron icon"></i>
            Previous
        </a>
    </#if>
    <#if nextURL??>
        <a class="ui right labeled icon button" href="${nextURL}">
            Next
            <i class="right chevron icon"></i>
        </a>
    <#else>
        <a class="ui right labeled icon button disabled">
            Next
            <i class="right chevron icon"></i>
        </a>
    </#if>
</div>

</div>

<div class="ui modal" id="create_dir">
    <i class="close icon"></i>
    <div class="header">
        Create directory
    </div>
    <div class="image content">
            <form class="description" action="/teacher/storage/create/directory" method="post">
                <div class="ui form">
                    <div class="inline fields">
                        <div class="eight wide field">
                            <label>Name</label>
                            <input type="text" placeholder="Directory name" name="name">
                        </div>
                    </div>
                </div>
                <#if id??>
                    <input type="hidden" name="parent" value="${id}">
                </#if>
                <div class="ui checkbox">
                    <input type="checkbox" id="make_public" checked>
                    <label>Make public</label>
                </div>
                <h4 class="header" style="margin-top: 5%;">Select groups to grant access to this directory</h4>
                <select name="access" multiple="" id="selector" class="ui fluid search disabled dropdown">
                    <option value="">Groups</option>
                    <#list groups as group>
                        <option value="${group.id}">${group.name}</option>
                    </#list>
                </select>
                <button style="margin-top: 5%;" class="ui positive right labeled icon button" type="submit">
                    Create
                    <i class="checkmark icon"></i>
                </button>
            </form>
    </div>
    <div class="actions">
        <div class="ui black deny button">
            Cancel
        </div>
    </div>
</div>

<div class="ui modal" id="edit_permissions">
    <i class="close icon"></i>
    <#if currentDirectory??>
        <div class="header">
            Edit permissions for ${currentDirectory}
        </div>
    </#if>
    <div class="image content">
        <form class="description" action="/teacher/storage/edit/permissions" method="post">
            <#if id??>
            <input type="hidden" name="id" value="${id}">
            </#if>
        <div class="ui checkbox">
            <input type="checkbox" id="make_public_permissions" <#if permissionsList??><#else>checked</#if>>
            <label>Make public</label>
        </div>
        <h4 class="header" style="margin-top: 5%;">Select groups to grant access to this directory</h4>
        <select name="access" multiple="" id="selector_edit" class="ui fluid search dropdown">
            <option value="">Groups</option>
            <#list groups as group>
             <option value="${group.id}" <#if permissionsList?? && permissionsList?seq_contains(group.id)>selected</#if> >${group.name}</option>
            </#list>
        </select>
        <button style="margin-top: 5%;" class="ui positive right labeled icon button" type="submit">
            Save
            <i class="checkmark icon"></i>
        </button>
        </form>
    </div>
    <div class="actions">
        <div class="ui black deny button">
            Cancel
        </div>
    </div>
</div>


<script src="/resources/main.js"></script>
</body>

</html>