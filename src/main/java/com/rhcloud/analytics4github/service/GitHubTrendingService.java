package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.exception.TrendingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses https://github.com/trending and gets  this month most popular repositories
 * Parser start on the class instantiation and saves data to an internal cachedTrendingRepos.
 *
 * @author lyashenkogs.
 * @since 9/3/16
 */
@Service
public class GitHubTrendingService {

//    static String GITHUB_TRENDING_URL = "https://github.com/trending?since=monthly";
        static String GITHUB_TRENDING_URL = "";
    private static Logger LOG = LoggerFactory.getLogger(GitHubTrendingService.class);
    private List<String> trendingRepos = new ArrayList<>();

    /**
     * Parses a GitHub trending web page and returns top 10 trending repositories
     *
     * @return top trending repositories list
     * @throws TrendingException can't get or parse the web page
     */
    public List<String> parseTrendingReposWebPage() throws TrendingException {
        try {
            Document webPageDocument = Jsoup.connect(GITHUB_TRENDING_URL).get();
            Elements elements = webPageDocument.select("h3>a");
            LOG.info("Top trending repositories according to " + GITHUB_TRENDING_URL);
            elements.forEach(element -> {
                LOG.info(element.attr("href"));
                trendingRepos.add(element.attr("href"));
            });
            return trendingRepos;
        } catch (Exception exception) {
            throw new TrendingException("Can't parse top trending repositories !", exception);
        }
    }
    public List<String> getTrendingRepos() {
        return trendingRepos;
    }

}
