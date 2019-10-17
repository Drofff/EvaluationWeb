<html>
<head>
    <title>Evaluation Web - My Grades</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/style.css">
</head>
<body>
<#include "parts/navbar.ftl">
<#if error_message??>
    <div class="ui negative message" style="width: 30%; margin-left: 35%; text-align: center;">
        <p>${error_message}</p>
    </div>
</#if>
<div class="ui segment grid">
    <div class="ui left fixed vertical menu" style="margin-top:5%;">
        <div class="item">
            <img class="ui tiny circular image" src="${photoUrl}" style="margin-left: 22%;">
            <h4 class="ui header">${name}</h4>
        </div>
        <a class="item" href="/test">My Tests</a>
        <a class="item">Grades</a>
        <a class="item" href="#">Teachers</a>
    </div>
    <div style="margin-left : 20%;">

        <h4 class="header" style="margin-top: 5%;">Total points: ${total}</h4>

        <table class="ui celled table" style="margin-top: 5%; margin-bottom: 15%;">
            <thead>
            <tr><th>Test</th>
                <th>Grade</th>
                <th>Date</th>
                <#list results as result>
            <tr>
                <td>
                    <h4 class="ui image header">
                        <div class="content">
                            ${result.name}
                            <div class="sub header">${result.numberOfQuestions} questions</div>
                        </div>
                    </h4>
                </td>
                <td>
                    <div class="ui tiny statistics">
                        <div class="yellow statistic">
                            <div class="value">
                                ${result.grade}
                            </div>
                        </div>
                    </div>
                </td>
                <td>
                    ${result.dateTime}
                </td>
            </tr>
            </#list>
            </tbody>
        </table>

    </div>
</div>
</body>
</html>