<html>
<head>
	<title>Evaluation Web</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
	<style>
		.materials{
			float: right;
			width: 45%;
			right: 3%;
			padding: 50px;
			height: 83.5%;
		}
		.groups, .subject{
			display: inline-block;
			width: 22%;
		}
		.groups{
			left: 5%;
		}
		.subject{
			left: 6%;
		}
		.search-link{
			text-decoration: none;
			color: black;
			padding: 15px;
		}
		.right.floated.ui.large.ellipsis.horizontal.icon:hover{
			color: rgb(42, 117, 201);
		}
		.test-list.button-group.right.floated{
			display: none;
			background-color: rgba(115, 129, 145, 0.4);
			border-radius: 4px;
		}
		.test-list.delete-button{
			width: 50%;
			background-color: inherit;
			padding: 5px;
			border: none;
			border-radius: 2px;
		}
		.test-list.delete-button.button-1:hover{
			background-color: rgb(219, 42, 33);
			color: white;
		}
		.test-list.delete-button.button-2:hover{
			background-color: rgb(42, 173, 38);
			color: white;
		}



	</style>

</head>
<body>
<#include "parts/teacherNavbar.ftl">
<#if error_message??>
	<div class="ui warning message" style="margin-left:10%; margin-right:10%;">
		<div class="header">
            ${error_message}
		</div>
	</div>
</#if>
<div class="ui tabular menu">
	<a class="item" href="/test/create">
		Create Test
	</a>
	<a class="active item">
		Edit/Delete Tests
	</a>
</div>

<div class="ui grid" style="margin-left: 2%; margin-top: 2%;">
	<div>
		<div class="ui cards">
			<#list tests as test>
				<div class="card">
					<div class="content">
						<i class="right floated ui large ellipsis horizontal icon" id="test${test.id}" onclick="get_selector('test${test.id}')"></i>
						<div id="test${test.id}list" class="test-list button-group right floated" style="width: 42%;">
							<button class="test-list delete-button button-1" onclick="delete_test('${test.id}')" style="margin-left: -0.5%">Delete</button>
							<button class="test-list delete-button button-2" onclick="update_test('${test.id}')" type="submit" style="margin-left: -3.5%">Edit</button>
						</div>
						<div class="header">${test.getName()}</div>
						<div class="description">
							<a>Questions:</a> ${test.getNumberOfQuestions()}
							<br/>
							<a>Duration:</a> ${test.getDuration()} minutes
							<br/>
							<a>Starts at:</a> <#if test.active> Started <#else> ${test.getStartTime()} </#if>
							<br/>
							<a>Deadline:</a> ${test.getDeadLine()}
						</div>
					</div>
					<#if test.active>
						<a class="ui red bottom attached button" href="/test/active/${test.id}/false">
							Deactivate
						</a>
					<#else>
						<a class="ui blue bottom attached button" href="/test/active/${test.id}/true">
							Activate
						</a>
                    </#if>
				</div>
            </#list>

		</div>
	</div>

</div>


<script type="text/javascript">
    let icon = $('.right.floated.ui.large.ellipsis.horizontal.icon');
    let buttonGroup = $('.test-list.button-group.right.floated');
    let card = $('.card');

    card.mouseleave(function(){
        icon.css("display", "block");
        buttonGroup.css("display", "none");
    });

    function get_selector(test_id) {
        var test_icon = $("#" + test_id);
        var test_list = $("#" + test_id + "list");
        test_icon.css("display", "none");
        test_list.css("display", "block");
    }

    function delete_test(id) {
        window.location.replace('/test/delete/' + id);
    }

    function update_test(id) {
        window.location.replace('/test/update/' + id);
    }

</script>
</body>

</html>
