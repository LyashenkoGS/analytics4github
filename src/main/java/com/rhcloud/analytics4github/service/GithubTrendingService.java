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
 * Parses https://github.com/trending and gets  this month most popular repositories
 * Parser start on the class instantiation and saves data to an internal cache.
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

    public List<String> getThisMonthTrendingRepos() {
        try {
            Document doc = Jsoup.connect("https://github.com/trending?since=monthly").get();
            Elements elements = doc.select("h3>a");
            List<String> trendingRepos = new ArrayList<>();
            elements.forEach(element -> {
                LOG.info(element.attr("href"));
                trendingRepos.add(element.attr("href"));
            });
            return trendingRepos;
        } catch (Exception ex) {
            LOG.error("CAN'T GET TRENDING REPOS!!");
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getTrendingRepos() {
        return trendingRepos;
    }
}
