package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.util.GithubApiIterator;
import com.rhcloud.analytics4github.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lyashenkogs.
 */
@Service
public class UniqueContributorsService {
    private static Logger LOG = LoggerFactory.getLogger(UniqueContributorsService.class);

    @Autowired
    private RestTemplate restTemplate;

    boolean isUniqueContributor(String repository, Author author, Instant uniqueSince) {
        String queryByAuthorName = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                .path(repository)
                .path("/" + GitHubApiEndpoints.COMMITS.toString().toLowerCase())
                .queryParam("author", author.getName())
                .queryParam("until", uniqueSince)
                .build().encode()
                .toUriString();
        LOG.debug(queryByAuthorName);
        JsonNode commitsPage = restTemplate.getForObject(queryByAuthorName, JsonNode.class);
        LOG.debug(commitsPage.toString());
        //if  an author seems to be unique, check one more time by author email
        if (!commitsPage.has(0)) {//return true if a commits page look like []
            LOG.debug("No commits by an author name: " + author.getName() + " untill " + uniqueSince);
            LOG.debug("Recheck by the author email: " + author.getEmail());
            String queryByAuthorEmail = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(repository)
                    .path("/" + GitHubApiEndpoints.COMMITS.toString().toLowerCase())
                    .queryParam("author", author.getEmail())
                    .queryParam("until", uniqueSince)
                    .build().encode()
                    .toUriString();
            LOG.debug(queryByAuthorEmail);
            commitsPage = restTemplate.getForObject(queryByAuthorEmail, JsonNode.class);
            LOG.debug(commitsPage.toString());
            return !commitsPage.has(0);//return true if a commits page look like []
        } else return false;
    }

    List<JsonNode> getCommits(String repository, Instant since) throws URISyntaxException, ExecutionException, InterruptedException {
        GithubApiIterator githubApiIterator = new GithubApiIterator(repository, restTemplate, GitHubApiEndpoints.COMMITS, since);
        List<JsonNode> commitPages = new ArrayList<>();
        List<JsonNode> commits = new ArrayList<>();
        while (githubApiIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = githubApiIterator.next(5);
            commitPages.addAll(commitPagesBatch);
        }
        githubApiIterator.close();
        LOG.debug(commitPages.toString());
        for (JsonNode page : commitPages) {
            for (JsonNode commit : page) {
                commits.add(commit);
                LOG.debug(commit.toString());
            }
        }

        return commits;
    }

    Set<Author> getAuthorNameAndEmail(List<JsonNode> commits) {
        Set<Author> authors = new HashSet<>();
        for (JsonNode commit : commits) {
            JsonNode authorEmail = commit.get("commit")
                    .get("author").get("email");
            JsonNode authorName = commit.get("commit")
                    .get("author").get("name");
            LOG.debug(authorEmail.textValue());
            LOG.debug(authorName.textValue());
            Author author = new Author(authorName.textValue(), authorEmail.textValue());
            authors.add(author);
        }
        LOG.debug("Authors : " + authors);
        return authors;
    }

    Set<Author> getUniqueContributors(String projectName, Instant uniqueSince) throws InterruptedException, ExecutionException, URISyntaxException {
        Set<Author> authorsPerPeriod = getAuthorNameAndEmail(getCommits(projectName, uniqueSince));
        Set<Author> newAuthors = authorsPerPeriod.parallelStream()
                .filter(e -> isUniqueContributor(projectName, e, uniqueSince))
                .collect(Collectors.toSet());
        LOG.debug("since" + uniqueSince + "  are " + newAuthors.size() + " new authors: " + newAuthors.toString());
        return newAuthors;
    }

    LocalDate getFirstContributionDate(Author author, String repository) {
        int reliableLastPageNumber = 0;
        String URL;
        if (author.getEmail() != null && !author.getEmail().isEmpty()) {
            reliableLastPageNumber = Utils.getLastPageNumber(repository, restTemplate, GitHubApiEndpoints.COMMITS, author.getEmail(), null);
            URL = UriComponentsBuilder
                    .fromHttpUrl("https://api.github.com/repos/")
                    .path(repository).path("/" + GitHubApiEndpoints.COMMITS.toString().toLowerCase())
                    .queryParam("page", reliableLastPageNumber)
                    .queryParam("author", author.getEmail())
                    .build().encode()
                    .toUriString();
        } else {
            reliableLastPageNumber = Utils.getLastPageNumber(repository, restTemplate, GitHubApiEndpoints.COMMITS, author.getName(), null);
            URL = UriComponentsBuilder
                    .fromHttpUrl("https://api.github.com/repos/")
                    .path(repository).path("/" + GitHubApiEndpoints.COMMITS.toString().toLowerCase())
                    .queryParam("page", reliableLastPageNumber)
                    .queryParam("author", author.getName())
                    .build().encode()
                    .toUriString();
        }
        LOG.debug(String.valueOf(reliableLastPageNumber));
        LOG.info(URL);
        JsonNode commitsPage = restTemplate.getForObject(URL, JsonNode.class);
        for (JsonNode commit : commitsPage) {
            LOG.debug(commit.toString());
        }
        List<JsonNode> commits = StreamSupport.stream(commitsPage.spliterator(), false).collect(Collectors.toList());
        JsonNode commit;
        try {
            commit = commits.get(commits.size() - 1);
            LOG.info("First commit by " + author + " : " + commit.toString());
            String date = commit.get("commit").get("author").get("date").textValue();
            LOG.debug(date);
            LocalDate firstContributionDate = Utils.parseTimestamp(date);
            LOG.info(firstContributionDate.toString());
            return firstContributionDate;
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.error("Cant properly get commits for :" + author);
            ex.printStackTrace();
            throw ex;
        }
    }

    List<LocalDate> getFirstAuthorCommitFrequencyList(String repository, Instant since) throws InterruptedException, ExecutionException, URISyntaxException {
        Set<Author> uniqueContributors = getUniqueContributors(repository, since);
        List<LocalDate> firstAuthorCommitFrequencyList = new ArrayList<>();
        for (Author author : uniqueContributors) {
            try {
                firstAuthorCommitFrequencyList.add(getFirstContributionDate(author, repository));
            } catch (ArrayIndexOutOfBoundsException ex) {
                LOG.error("cant properly getFirstContributionDate :" + author);
                LOG.error("don't add any to firstAuthorCommitFrequencyList for " + repository);
            }
        }
        LOG.info(firstAuthorCommitFrequencyList.toString());
        return firstAuthorCommitFrequencyList;
    }

    public ArrayNode getUniqueContributorsFrequencyByMonth(String repository) throws IOException, ClassNotFoundException, InterruptedException, ExecutionException, URISyntaxException {
        TreeMap<LocalDate, Integer> commitsFrequencyMap = Utils.buildStargazersFrequencyMap(getFirstAuthorCommitFrequencyList
                (repository, Utils.getThisMonthBeginInstant()));
        List<Integer> frequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(commitsFrequencyMap);
        ArrayNode buildedJsonForHighChart = Utils.buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }

    public ArrayNode getUniqueContributorsFrequencyByWeek(String repository) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        List<LocalDate> firstAuthorCommitFrequencyList = getFirstAuthorCommitFrequencyList(repository, Utils.getThisWeekBeginInstant());
        TreeMap<LocalDate, Integer> weekContributorsFrequencyMap = Utils.buildStargazersFrequencyMap(firstAuthorCommitFrequencyList);
        List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(weekContributorsFrequencyMap);
        ArrayNode buildedJsonForHighChart = Utils.buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }
}
