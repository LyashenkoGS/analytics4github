/**
 * Created by iron on 24.12.16.
 */

/**
 * This method display random trending repository to analyze
 */
(function () {
    $('#random-repo-btn').click(function () {
        $.ajax({
            //thoughout front-end development  use http://localhost:8080/randomRequestTrendingRepoName" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
            url: "/randomRequestTrendingRepoName"
        })
            .done(function (msg) {
                var response = msg;
                $('#projectName').text(msg);
                $('#projectName').val(msg);
                $('#projectName').attr("href",
                    "https://github.com/"
                    + msg)
                console.log(msg)
                $('#repository').text(msg);
                $('#repository').attr("href", "https://github.com" + msg)
            })
            .fail(function (jqXHR, textStatus) {
                console.log(textStatus);
                $('#month-frequency-plot')
                    .html("<div class='alert alert-warning' role='alert'>Request failed with status:"
                        + textStatus
                        + "<div>Sorry for temporary inconvenience<div></div>");
            });
    });
})();

var date = new Date();
/**
 * Display interval from the current month begin to the current month end on UI
 */
function displayCurrentDate() {
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
displayCurrentDate();

/**
 * Display interval from the previous month on UI
 */
$("#previousDate").click(
    function () {
        date.setMonth(date.getMonth() - 1);
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
        date.setMonth(date.getMonth() + 1);
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
(function () {
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
})();

$(function () {
    $(function () {
        $('#projectName').on('input', function (e) {
            console.log($(this).val());
            $('#repository').text($(this).val());
            $('#repository').attr("href", "https://github.com/" + $(this).val())
        });
    });

    /**
     * Get stargazers per week json and display as a column chart
     */
    $(function () {
        $('#analyze-btn').click(function () {
            var inputValue = $('#projectName').val();
            console.log(inputValue);
            var active_tab = $("ul.nav-tabs .active").attr('id');
            console.log("active tab: " + active_tab);
            $.ajax({
                //thoughout front-end development  use http://localhost:8080/stargazers" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
                url: "/" + active_tab + "?projectName="
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
                .fail(function (jqXHR, textStatus) {
                    $('#week-frequency-plot')
                        .html("<div class='alert alert-warning' role='alert'>Request failed with status:"
                            + textStatus
                            + "<div>Sorry for temporary inconvenience<div></div>");
                });

            $.ajax({
                //thoughout front-end development  use http://localhost:8080/stargazers" + "?projectName=" + inputValue/stargazers" + "?projectName=" + inputValue
                url: "/" + active_tab + "PerMonth" + "?projectName="
                + inputValue,
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
                .fail(function (jqXHR, textStatus) {
                    $('#month-frequency-plot')
                        .html("<div class='alert alert-warning' role='alert'>Request failed with status:"
                            + textStatus
                            + "<div>Sorry for temporary inconvenience<div></div>");
                });
        })
    })
});
