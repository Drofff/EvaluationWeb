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
    <a class="item active">
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
    <div class="right menu">
<!--        <div class="ui action input">-->
<!--            <input type="text" placeholder="Find test...">-->
<!--            <div class="ui button">Search</div>-->
<!--        </div>-->
        <div class="item">
            <a class="ui primary button" href="/logout">Log out</a>
        </div>
    </div>

</div>
<#if week??>
<#assign prevWeek = week - 1
         nextWeek = week + 1 >
</#if>
<div class="ui segment grid">

    <p>
    <div class="four wide column" style="margin-bottom:10%;">
    <div class="ui vertical pointing menu">
    <a class="item <#if week?? && week gt 0><#else>disabled</#if>" <#if week?? && week gt 0> href="/?week=${prevWeek}" </#if> >
        Previous week <i class="angle up icon"></i>
    </a>
        <a class="item <#if day?? && day == 1>active</#if>" <#if week??> href='?week=${week}&day=1' <#else> href='?day=1' </#if> >
            Monday <#if today?? && week?? && today == 1 && week == 0>(Today)</#if> <#if test_at_1??><div class="ui label">Test</div></#if>
        </a>
        <a class="item <#if day?? && day == 2>active</#if>" <#if week??> href='?week=${week}&day=2' <#else> href='?day=2' </#if> >
            Tuesday <#if today?? && week?? && today == 2 && week == 0>(Today)</#if> <#if test_at_2??><div class="ui label">Test</div></#if>
        </a>
        <a class="item <#if day?? && day == 3>active</#if>" <#if week??> href='?week=${week}&day=3' <#else> href='?day=3' </#if> >
            Wednesday <#if today?? && week?? && today == 3 && week == 0>(Today)</#if> <#if test_at_3??><div class="ui label">Test</div></#if>
        </a>
        <a class="item <#if day?? && day == 4>active</#if>" <#if week??> href='?week=${week}&day=4' <#else> href='?day=4' </#if> >
            Thursday <#if today?? && week?? && today == 4 && week == 0>(Today)</#if> <#if test_at_4??><div class="ui label">Test</div></#if>
        </a>
        <a class="item <#if day?? && day == 5>active</#if>" <#if week??> href='?week=${week}&day=5' <#else> href='?day=5' </#if> >
            Friday <#if today?? && week?? && today == 5 && week == 0>(Today)</#if> <#if test_at_5??><div class="ui label">Test</div></#if>
        </a>
    <a class="item <#if week?? && week gt 2>disabled</#if>" <#if week?? && week lt 3> href="/?week=${nextWeek}" </#if>>
        Next week <i class="angle down icon"></i>
    </a>
    </div>
</div>
<div class="eleven wide column">
<#if todayDate??>
<h5 class="ui header">${todayDate}</h5>
</#if>
<#if lessons?? && lessons?size gt 0 >
<div class="ui grid">
    <#list lessons as lesson>
    <div class="eight wide column">
<div class="ui card">
    <div class="content">
        <div class="header">${lesson.title}</div>
    </div>
    <div class="content">
        <h4 class="ui sub header">Lesson</h4>
        <div class="ui small feed">
            <#if lesson.test?? && lesson.test>
                <div class="event">
                    <div class="content">
                        <div class="summary">
                            <h4 class="ui red header">You will have test during this lesson</h4>
                        </div>
                    </div>
                </div>
            </#if>
            <div class="event">
                <div class="content">
                    <div class="summary">
                        <a>Time:</a> ${lesson.time} (${lesson.duration} minutes)
                    </div>
                </div>
            </div>
            <div class="event">
                <div class="content">
                    <div class="summary">
                        <a>Room:</a> ${lesson.room}
                    </div>
                </div>
            </div>
        <#if lesson.homeTask??>
            <div class="event">
                <div class="content">
                    <div class="summary">
                        <a>Home work:</a> ${lesson.homeTask}
                    </div>
                </div>
            </div>
        </#if>
        <#if lesson.description??>
            <div class="event">
                <div class="content">
                    <div class="summary">
                        <a>Description:</a> ${lesson.description}
                    </div>
                </div>
            </div>
        </#if>
            <div class="event">
                <div class="content">
                    <div class="summary">
                        <a>Teacher:</a> ${lesson.teacherFullName}
                    </div>
                </div>
            </div>
        </div>
    </div>
        <#if isTeacher?? && isTeacher>
        <div class="extra content">
            <form action="/teacher/delete" method="post">
                <input type="hidden" name="lessonId" value="${lesson.id}">
                <button type="submit" class="ui button">
                    Delete
                </button>
                <a class="ui button" href="/teacher/edit/${lesson.id}">Edit</a>
            </form>
        </div>
        </#if>
</div>
</div>
    </#list>
</div>
<#else>
    <#if vacation??>
        <div class="ui violet message">
            <i class="hand peace outline icon"></i> It is weekend now! The best time to study
        </div>
    <#else>
        <div class="ui icon message">
            <i class="calendar outline icon"></i>
            <div class="content">
                <div class="header">
                    You have not any lessons for this day
                </div>
                <p>Enjoy your studying!</p>
            </div>
        </div>
    </#if>
</#if>
</div>
    </p>
</div>
</body>

</html>