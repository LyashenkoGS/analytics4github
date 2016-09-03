package com.rhcloud.analytics4github.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse https://github.com/trending and get  this month most popular repositories
 * Parser start on the class instantiation and save date to a internal cache.
 *
 * @author lyashenkogs.
 * @since 9/3/16
 */
@Service
public class GithubTrendingService {
    private static Logger LOG = LoggerFactory.getLogger(GithubTrendingService.class);

    private final List<String> trendingRepos;

    public GithubTrendingService() {
        trendingRepos = getThisMonthTrendingRepos();

    }

    private List<String> getThisMonthTrendingRepos() {
        try {
            Document doc = Jsoup.connect("https://github.com/trending?since=monthly").get();
            Elements elements = doc.select("h3.repo-list-name>a");
            List<String> trendingRepos = new ArrayList<>();
            elements.forEach(e -> {
                LOG.info(e.attr("href"));
                trendingRepos.add(e.attr("href"));
            });
            return trendingRepos;
        } catch (Exception ex) {
            LOG.error("CANT GET TRENDING REPOS!!");
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getTrendingRepos() {
        return trendingRepos;
    }
}
