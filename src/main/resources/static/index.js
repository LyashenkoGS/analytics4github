/**
 * Created by Nazar on 27.12.2016.
 */
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
                    // var exception = jQuery.parseJSON(jqXHR.responseText);

                    $('#month-frequency-plot')
                        .html("<div class='alert alert-warning' role='alert'>Request failed with status:"
                            + textStatus+
                            + "<div>Sorry for temporary inconvenience<div></div>");
                });
        })
    })

});
