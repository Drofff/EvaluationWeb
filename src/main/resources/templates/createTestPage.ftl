<html>
<head>
    <title>Evaluation Web</title>
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
<#if messageError??>
<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${messageError}
    </div>
</div>
</#if>
<#if messageSuccess??>
<div class="ui positive message" style="margin-left:10%; margin-right:10%;">
    <div class="header">
        ${messageSuccess}
    </div>
</div>
</#if>
<div class="ui segment">

    <form class="ui form" action="/test/create" method="post" style="width: 60%; margin-left: 10%; margin-right:10%;">
        <h4 class="ui dividing header">Test Information</h4>
        <div class="field" style="width: 510px;">
            <label>Name</label>
                <div class="field">
                    <input type="text" name="name" placeholder="Test name" pattern="[A-Za-zА-Яа-яЁё-іІїЇєЄ,\D, 0-9]{5,}" required>
                </div>
        </div>
	    <div class="field" style="width: 510px;">
		    <label>Groups</label>
		    <select name="groups" multiple="" id="groups_selector" class="ui fluid search dropdown">
			    <option value="">Select group</option>
                <#list my_groups as group>
				    <option value="${group.name}">${group.name}</option>
                </#list>
		    </select>
	    </div>
        <div class="field" style="display: inline-block;width: 510px;">
            <label>Duration</label>
            <div class="ui right labeled input">
                <input type="number" name="duration" placeholder="Duration" required>
                <div class="ui basic label">
                    Minutes
                </div>
            </div>
        </div>
        <br>
        <div class="field" style="display: inline-block">
            <label>Deadline</label>
            <div class="field">
                <input type="datetime-local" name="deadLine" required>
            </div>
        </div>

        <div class="field" style="display: inline-block">
            <label>Start time</label>
            <div class="field">
                <input type="datetime-local" name="startTime" required>
            </div>
        </div>
        <div class="ui divider"></div>
        <div class="field" style=" width: 510px;">
            <label>Question:</label>
            <div class="field">
                <input type="text" name="q1" required>
            </div>
        </div>
        <div class="field" style="margin-left: 5%;">
            <label>Answers:</label>
            <div class="field testAnswer">
                <input type="text" name="a1q1" required>
            </div>
            <div class="ui checkbox testCheckbox">
                <input type="checkbox" name="a1q1r" title="Check right answer">
                <label></label>
            </div>

            <div id="new_answ_q1">
                <a class="ui icon button" onclick="add_answer(1)" style="display: block; width: 40px">
                    <i class="plus icon"></i>
                </a>
            </div>

        </div>
        <div class="ui divider"></div>
        <div id="new_question" style="margin-bottom: 1%">
            <a class="ui icon button" onclick="add_question()">
                <i class="plus icon"></i>
            </a>
        </div>


        <button class="ui primary button" type="submit">
            Create
        </button>

    </form>

</div>
<script src="/resources/main.js"></script>
</body>

</html>
