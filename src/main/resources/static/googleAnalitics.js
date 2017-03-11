/**
 * Created by iron on 24.12.16.
 */

(function (i, s, o, g, r, a, m) {
    i['GoogleAnalyticsObject'] = r;
    i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
    a = s.createElement(o),
        month = s.getElementsByTagName(o)[0];
    a.async = 1;
    a.src = g;
    month.parentNode.insertBefore(a, month)
})(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

ga('create', 'UA-81812642-2', 'auto');
ga('send', 'pageview');
