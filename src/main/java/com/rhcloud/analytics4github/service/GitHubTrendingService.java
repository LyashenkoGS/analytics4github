package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.exception.TrendingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    static String GITHUB_TRENDING_URL = "https://github.com/trending?since=monthly";
    private static Logger LOG = LoggerFactory.getLogger(GitHubTrendingService.class);
    List<String> trendingRepos = new ArrayList<>();

    /**
     * Parses a GitHub trending web page and returns top 10 trending repositories
     *
     * @return top trending repositories list
     * @throws TrendingException can't get or parse the web page
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     *  Method parseTrendingReposWebPage that added repo to the cash will be invoked every 2 minutes.
     */
    @Scheduled(fixedRate = 120000)
    public void parseTrendingReposWebPage() throws TrendingException {
        LOG.info("The time is now {}", dateFormat.format(new Date()));
        try {
            Document webPageDocument = Jsoup.connect(GITHUB_TRENDING_URL).get();
            Elements elements = webPageDocument.select("h3>a");
            LOG.info("Top trending repositories according to " + GITHUB_TRENDING_URL);
            elements.forEach(element -> {
                LOG.info(element.attr("href"));
                trendingRepos.add(element.attr("href"));
            });
        } catch (Exception exception) {
            throw new TrendingException(" Can not access GitHub Trending page <a href='https://github.com/trending'>https://github.com/trending<a/>" ,exception);
        }
    }

    public List<String> getTrendingRepos() throws TrendingException{
        if(!trendingRepos.isEmpty()){
            return trendingRepos;
        }else   throw new TrendingException(" Can not access GitHub Trending page <a href='https://github.com/trending'>https://github.com/trending</a> " );
    }
}
