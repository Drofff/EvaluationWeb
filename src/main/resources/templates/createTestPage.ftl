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
	<script>
        var questionIndexRegex = /.*q([0-9]+).*/;
        var questionRegex = /^q([0-9]+)$/;
        $(function() {
            var params = [];
            <#if oldParams??>
            <#list oldParams as key, value>
            var key = '${key}';
            var value = '${value}';
            if(key.match(questionIndexRegex)) {
                params.push([key, value]);
            }
            </#list>
            </#if>
            var indexes = getAllIndexes(params);
            indexes.forEach(function(index) {
                addQuestionWithAnswers(index, params);
            });
        });

        function getAllIndexes(params) {
            var indexes = [];
            params.forEach(function(param) {
                var question = param[0].match(questionRegex);
                if(question && question[1] > 1) {
                    indexes.push(question[1]);
                }
            });
            return indexes;
        }

        function addQuestionWithAnswers(index, params) {
            var question = parseQuestion(index, params);
            var answers = parseAnswers(index, params);
            add_pre_defined_question(question, answers);
        }

        function parseQuestion(index, params) {
            var name = 'q' + index;
            var question = '';
            params.forEach(function(param) {
                if(param[0] === name) {
                    question = param[1];
                }
            });
            return question;
        }

        function parseAnswers(questionIndex, params) {
            var answerPattern = new RegExp('^a(\\d+)q' + questionIndex + '$');
            var answers = [];
            params.forEach(function(param) {
                if(param[0].match(answerPattern)) {
                    var is_right = is_right_answer(param[0], params);
                    var answer = {
                        'name' : param[0],
                        'value' : param[1],
                        'is_right' : is_right
                    };
                    answers.push(answer);
                }
            });
            return answers;
        }

        function is_right_answer(answerName, params) {
            var rightAnswerName = answerName + 'r';
            var isRight = false;
            params.forEach(function(param) {
                if(param[0] === rightAnswerName) {
                    isRight = true;
                }
            });
            return isRight;
        }
        function delete_first_answer(){
            alert('You can\'t delete first answer');
        }

        function delete_first_question() {
            alert('You can\'t delete first question');
        }
	</script>
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
<div class="ui tabular menu">
	<a class="active item">
		Create Test
	</a>
	<a class="item" href="/test/manage">
		Edit/Delete Tests
	</a>
</div>
<div style="margin-top: 2%;">

	<form class="ui form" action="/test/create" method="post" style="width: 60%; margin-left: 10%; margin-right:10%;">
		<div class="field" style="width: 510px;">
			<label>Name</label>
			<div class="field <#if nameError??>error</#if>">
				<input type="text" <#if enteredValue?? && enteredValue.name??>value="${enteredValue.name}"</#if> name="name" placeholder="Test name" pattern="[A-Za-zА-Яа-яЁё-іІїЇєЄ,\D, 0-9]{1,}" required>
                <#if nameError??><p style="color:red">${nameError}</p></#if>
			</div>
		</div>
		<div class="field <#if groupError??>error</#if>" style="width: 510px;">
			<label>Groups</label>
			<select name="groups" multiple="" id="groups_selector" class="ui fluid search dropdown">
				<option value="">Select group</option>
                <#list my_groups as group>
					<option <#if selectedGroupsNames?? && selectedGroupsNames?seq_contains(group.name)>selected</#if> value="${group.name}">${group.name}</option>
                </#list>
			</select>
            <#if groupError??><p style="color:red">${groupError}</p></#if>
		</div>
		<div class="field <#if durationError??>error</#if>" style="width: 510px;">
			<label>Duration</label>
			<div class="ui right labeled input">
				<input type="number" <#if enteredValue?? && enteredValue.duration??>value="${enteredValue.duration}"</#if> name="duration" placeholder="Duration" required>
				<div class="ui basic label">
					Minutes
				</div>
			</div>
            <#if durationError??><p style="color:red">${durationError}</p></#if>
		</div>
		<br>
		<div class="field <#if deadlineError??>error</#if>" style="display: inline-block">
			<label>Deadline</label>
			<div class="field">
				<input type="datetime-local" name="deadLine" <#if enteredValue?? && enteredValue.deadLine??>value="${enteredValue.deadLine}"</#if> required>
                <#if deadlineError??><p style="color:red">${deadlineError}</p></#if>
			</div>
		</div>

		<div class="field <#if startTimeError??>error</#if>" style="display: inline-block">
			<label>Start time</label>
			<div class="field">
				<input type="datetime-local" name="startTime" <#if enteredValue?? && enteredValue.startTime??>value="${enteredValue.startTime}"</#if> required>
			</div>
		</div>
		<div class="ui divider"></div>
		<div class="field" style=" width: 510px;">
			<label>Question:</label>
			<div class="field">
				<input type="text" name="q1" <#if oldParams?? && oldParams.q1??>value="${oldParams.q1}"</#if> required>
			</div>
		</div>
		<div class="field" style="margin-left: 5%;">
			<label>Answers:</label>
			<div class="field testAnswer">
				<input type="text" name="a1q1" <#if oldParams?? && oldParams.a1q1??>value="${oldParams.a1q1}"</#if> required>
			</div>
			<div class="ui checkbox testCheckbox">
				<input type="checkbox" name="a1q1r" <#if oldParams?? && oldParams.a1q1r??>checked</#if> title="Check right answer">
				<label></label>
			</div>

            <#if firstQuestionAdditionalAnswers??>
                <#list firstQuestionAdditionalAnswers as answer>
					<div class="delete-block-answer">
						<button type="button" class="ui mini red icon button answer-delete-button" name='${answer.name}' onclick="delete_answer(this)">
							<i class="delete icon"></i>
						</button>
						<div class="field testAnswer">
							<input type="text" name="${answer.name}" value="${answer.answer}" required>
						</div>
						<div class="ui checkbox testCheckbox">
							<input type="checkbox" name="${answer.name}r" <#if answer.right>checked</#if> title="Check right answer">
							<label></label>
						</div>
					</div>
                </#list>
            </#if>

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


		<button class="ui primary button" type="submit" style="margin-top: 5%; margin-bottom: 2%;">
			Create
		</button>

	</form>

</div>
<script src="/resources/main.js"></script>
</body>

</html>
