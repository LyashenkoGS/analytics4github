/**
 *  This file include all JavaScript methods that renders UI
 * and sends ajax requests to backend REST endpoints
 */

//Global variables
var date = new Date();
var endPeriod = new Date();

/**
 * Performs a call to the backend  GET "/getRequestsLeft" and displays a number of requests that left on UI
 */
function renderNumberOfRequestsLeft() {
    $.ajax({
        url: "getRequestsNumberLeft",
        success: function (data) {
            document.getElementById('requestsLeft').innerHTML = "Requests Left: " + data;
        }
    })
}

/**
 * Display a charts based on analytics result.
 * 1. performs a REST call to analyze stargazers|commits|stars per week
 * 2. performs a REST call to analyze stargazers|commits|stars per month
 * implicit parameters are: currently selected or clicked tab on UI and currently selected month on UI
 */
function analyze(e) {
    var inputValue = $('#projectName').val();
    console.log("repository to analyse: " + inputValue);
    var analyticsArea;
    if (e === undefined || $(this).attr('id') == 'analyze-btn') {
        analyticsArea = $('ul.nav-tabs .active').attr('id');
        console.log("active tab: " + analyticsArea);
    }
    else {
        analyticsArea = $(this).attr('id');
        console.log("clicked tab: " + analyticsArea);
    }
    //render frequency chart per week
    $.ajax({
        //thoughout front-end development  use http://localhost:8080/stargazers" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
        url: "/" + analyticsArea + "?projectName="
        + inputValue,
        beforeSend: function () {
            $('#week-frequency-plot')
                .html("<img src='https://assets-cdn.github.com/images/spinners/octocat-spinner-128.gif' /> <div>Crunching the latest date, just for you. </div>");
        }
    })
        .done(function (msg) {
            var response = msg;
            $('#week-frequency-plot').highcharts({
                chart: {
                    type: 'line',
                    height: '200'
                },
                legend: {
                    enabled: false
                },
                title: {
                    style: {
                        "display": "none"
                    }
                },
                xAxis: {
                    categories: [
                        'Mon',
                        'Tue',
                        'Wed',
                        'Thu',
                        'Fri',
                        'Sat',
                        'Sun'
                    ]
                },
                yAxis: {
                    min: 0,
                    tickInterval: 1
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
                    +
                    '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    series: {
                        color: '#1db34f'
                    }
                },
                series: response
            });
        })
        .fail(function (jqXHR) {
            $('#week-frequency-plot')
                .html("<div class='alert alert-danger' role='alert'>Request failed with status:"
                    + jqXHR.responseText
                    + "<div>Sorry for temporary inconvenience<div></div>");
        });
    //render frequency chart per month
    $.ajax({
        //thoughout front-end development  use http://localhost:8080/stargazers" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
        url: "/" + analyticsArea + "PerMonth" + "?projectName="
        + inputValue + "&startPeriod=" + parseDateToISOString(date) + "&endPeriod=" + parseDateToISOString(endPeriod),
        beforeSend: function () {
            $('#month-frequency-plot')
                .html("<img src='https://assets-cdn.github.com/images/spinners/octocat-spinner-128.gif' /> <div>Crunching the latest date, just for you. </div>");
        }
    })
        .done(function (msg) {
            var response = msg;
            $('#month-frequency-plot').highcharts({
                chart: {
                    type: 'column'
                },
                legend: {
                    enabled: false
                },
                title: {
                    style: {
                        "display": "none"
                    }
                },
                xAxis: {
                    tickInterval: 1,
                    min: 1
                },
                yAxis: {
                    min: 0
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
                    +
                    '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.1,
                        borderWidth: 0,
                        groupPadding: 0
                    },
                    series: {
                        color: '#f17f49'
                    }
                },
                series: response
            });
        })
        .fail(function (jqXHR) {
            $('#month-frequency-plot')
                .html("<div class='alert alert-danger' role='alert'>Request failed with status:"
                    + jqXHR.responseText
                    + "<div>Sorry for temporary inconvenience<div></div>");
        });
    renderNumberOfRequestsLeft();
}

/**
 * Displays  a random trending repository to analyze on a click to the "random-repo-btn"
 */
function renderRandomTrendingRepository() {
    $.ajax({
        //throughout front-end development  use http://localhost:8080/randomRequestTrendingRepoName" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
        url: "/randomRequestTrendingRepoName"
    })
        .done(function (msg) {
            $('#projectName').text(msg);
            $('#projectName').val(msg);
            $('#projectName').attr("href", "https://github.com/" + msg);
            console.log(msg)
            $('#repository').text(msg);
            $('#repository').attr("href", "https://github.com" + msg);
            analyze();
        })
        .fail(function (jqXHR, textStatus) {
            console.log(textStatus)
            console.log(jqXHR)
            $('#month-frequency-plot')
                .html("<div class='alert alert-danger' role='alert'>Request failed with status:"
                    + jqXHR.responseText
                    + "<div>Sorry for temporary inconvenience<div></div>");
        });
}

/**
 * On typing a repository to the input form copy it to #repository block on UI and forming it's valid URL
 */
$('#projectName').on('input', function () {
    console.log($(this).val());
    $('#repository').text($(this).val());
    $('#repository').attr("href", "https://github.com/" + $(this).val())
});

function parseDateToISOString(date) {
    var dateReturn = '';
    dateReturn += date.getFullYear() + '-';
    if ((date.getMonth() + 1).toString().length == 1) {
        dateReturn += 0;
        dateReturn += (date.getMonth() + 1) + '-';
    } else {
        dateReturn += (date.getMonth() + 1) + '-';
    }
    if (date.getDate().toString().length == 1) {
        dateReturn += 0;
        dateReturn += date.getDate();
    } else {
        dateReturn += date.getDate();
    }
    return dateReturn;
}

/**
 * Displays interval from the current month begin to the current month end on UI
 */
function displayCurrentDate() {
    date.setMonth(date.getMonth(), 1);
    date.setHours(00, 00, 00);
    endPeriod.setHours(23, 59, 59);
    endPeriod.setFullYear(date.getFullYear(), date.getMonth() + 1, 0);
    var year = date.getFullYear(), month = date.getMonth();
    var lastDay = new Date(year, month + 1, 0).getDate();
    var objDate = new Date(),
        locale = "en-us",
        month = objDate.toLocaleString(locale, {month: "short"});
    var intervalStart = month + " " + "01" + ", " + new Date().getFullYear();
    var intervalEnd = month + " " + lastDay + ", " + new Date().getFullYear();
    document.getElementById('current-month-interval').textContent
        = intervalStart + " - " + intervalEnd;
}

/**
 * Display interval from the previous month on UI
 */
$("#previousDate").click(
    function () {
        date.setMonth(date.getMonth() - 1, 1);
        endPeriod.setFullYear(date.getFullYear(), date.getMonth() + 1, 0);
        var year = date.getFullYear();
        var month = date.getMonth();
        var lastDay = new Date(year, month + 1, 0).getDate();
        var objDate = date,
            locale = "en-us",
            month = objDate.toLocaleString(locale, {month: "short"});
        var intervalStart = month + " " + "01" + ", " + date.getFullYear();
        var intervalEnd = month + " " + lastDay + ", " + date.getFullYear();
        document.getElementById('current-month-interval').textContent
            = intervalStart + " - " + intervalEnd;
    });

/**
 * Display interval from the next month on UI
 */
$("#nextDate").click(
    function () {
        date.setMonth(date.getMonth() + 1, 1);
        endPeriod.setFullYear(date.getFullYear(), date.getMonth() + 1, 0);
        var year = date.getFullYear();
        var month = date.getMonth();
        var lastDay = new Date(year, month + 1, 0).getDate();
        var objDate = date,
            locale = "en-us",
            month = objDate.toLocaleString(locale, {month: "short"});
        var intervalStart = month + " " + "01" + ", " + date.getFullYear();
        var intervalEnd = month + " " + lastDay + ", " + date.getFullYear();
        document.getElementById('current-month-interval').textContent
            = intervalStart + " - " + intervalEnd;
    });

/**
 * Display interval from the current week month  to the current saturday  on UI
 */
function displayInterval() {
    var curr = new Date; // get current date
    var firstDay = new Date(curr.setDate(curr.getDate() - curr.getDay()));// First day is the day of the month - the day of the week
    var last = new Date(curr.setDate(curr.getDate() - curr.getDay() + 6));// last day is the first day + 6
    var locale = "en-us";
    var startMonth = firstDay.toLocaleString(locale, {month: "short"});
    var endMonth = last.toLocaleString(locale, {month: "short"});
    document.getElementById('current-week-interval').textContent
        =
        startMonth + " " + firstDay.getDate() + ", " + firstDay.getFullYear()
        + " - " +
        endMonth + " " + last.getDate() + ", " + last.getFullYear();
}

//Functions that should start on UI loading
analyze();
displayInterval();
displayCurrentDate();
renderNumberOfRequestsLeft();

//Assigning functions to buttons
$('#analyze-btn').on('click', analyze);
$('#stargazers').on('click', analyze);
$('#commits').on('click', analyze);
$('#uniqueContributors').on('click', analyze);

$('#random-repo-btn').click(renderRandomTrendingRepository);
