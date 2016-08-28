package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.config.GtihubApiEndpoints;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.util.GithubApiIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author lyashenkogs.
 */
@Service
public class UniqueContributorsService {
    private static Logger LOG = LoggerFactory.getLogger(UniqueContributorsService.class);
    @Autowired
    RestTemplate restTemplate;

    public boolean isUniqueContributor(String repository, Author author, Instant uniqueSince) {
        String queryByAuthorName = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                .path(repository)
                .path("/" + GtihubApiEndpoints.COMMITS.toString().toLowerCase())
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
                    .path("/" + GtihubApiEndpoints.COMMITS.toString().toLowerCase())
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

    public List<JsonNode> getCommits(String repository, Instant since) throws URISyntaxException, ExecutionException, InterruptedException {
        GithubApiIterator githubApiIterator = new GithubApiIterator(repository, restTemplate, GtihubApiEndpoints.COMMITS, since);
        List<JsonNode> commitPages = new ArrayList<>();
        List<JsonNode> commits = new ArrayList<>();
        while (githubApiIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = githubApiIterator.next(5);
            commitPages.addAll(commitPagesBatch);
        }
        LOG.debug(commitPages.toString());
        for (JsonNode page : commitPages) {
            for (JsonNode commit : page) {
                commits.add(commit);
                LOG.debug(commit.toString());
            }
        }

        return commits;
    }

    public Set<Author> getAuthorNameAndEmail(List<JsonNode> commits) {
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

    public Set<Author> getUniqueContributors(String projectName, Instant uniqueSince) throws InterruptedException, ExecutionException, URISyntaxException {
        Set<Author> authorsPerPeriod = getAuthorNameAndEmail(getCommits(projectName, uniqueSince));
        Set<Author> newAuthors = authorsPerPeriod.parallelStream()
                .filter(e -> isUniqueContributor(projectName, e, uniqueSince))
                .collect(Collectors.toSet());
        LOG.debug("this month  are " + newAuthors.size() + " new authors: " + newAuthors.toString());
        return newAuthors;
    }

}
