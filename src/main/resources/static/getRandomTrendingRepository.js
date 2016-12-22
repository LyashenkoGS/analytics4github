/**
 * Created by iron on 21.12.16.
 */

/**
 * get random top trending repo and set in name to the main input field on the page
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
                $('#repository').attr("href", "https://github.com" +msg)
            })
            .fail(function (jqXHR, textStatus) {
                console.log(textStatus)
               // console.log(jqXHR)
                $('#month-frequency-plot')
                    .html("<div class='alert alert-warning' role='alert'>Request failed with status:"
                        + textStatus
                        + "<div>Sorry for temporary inconvenience<div></div>");
            });
    });
})()