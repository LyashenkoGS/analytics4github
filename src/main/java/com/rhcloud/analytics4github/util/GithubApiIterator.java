package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.config.GtihubApiEndpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Iterate over commits array of specified project on Github using RestTemplate
 *
 * @author lyashenkogs.
 */
public class GithubApiIterator implements Iterator<JsonNode> {
    private static Logger LOG = LoggerFactory.getLogger(GithubApiIterator.class);

    private final int numberOfPages;
    private final String projectName;
    private final Enum githubEndpoint;
    private final RestTemplate restTemplate;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Instant since = null;
    private volatile AtomicInteger counter = new AtomicInteger();


    public GithubApiIterator(String projectName, RestTemplate restTemplate, GtihubApiEndpoints endpoint
    ) throws URISyntaxException {
        this.restTemplate = restTemplate;
        this.projectName = projectName;
        this.githubEndpoint = endpoint;
        this.numberOfPages = getLastPageNumberByProjectName(projectName);
        this.counter.set(numberOfPages);
    }

    public GithubApiIterator(String projectName, RestTemplate restTemplate, GtihubApiEndpoints endpoint,
                             Instant since) throws URISyntaxException {
        this.restTemplate = restTemplate;
        this.projectName = projectName;
        this.githubEndpoint = endpoint;
        this.numberOfPages = getLastPageNumberByProjectName(projectName);
        this.counter.set(numberOfPages);
        this.since = since;

    }


    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getProjectName() {
        return projectName;
    }

    private int getLastPageNumberByProjectName(String projectName) throws URISyntaxException {
        String URL;

        if (since != null) {
            URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(projectName).path("/" + githubEndpoint.toString().toLowerCase())
                    .queryParam("since", since)
                    .build().encode()
                    .toUriString();
        } else {
            URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(projectName).path("/" + githubEndpoint.toString().toLowerCase())
                    .build().encode()
                    .toUriString();
        }
        LOG.debug("URL to get the last commits page number:" + URL);
        ResponseEntity<JsonNode> stargazersPageResponseEntity = restTemplate.getForEntity(URL, JsonNode.class);
        String link = stargazersPageResponseEntity.getHeaders().getFirst("Link");
        LOG.debug("Link: " + link);
        LOG.debug("parse link by regexp");
        Pattern p = Pattern.compile("page=(\\d*)>; rel=\"last\"");
        int lastPageNum = 0;
        try {
            Matcher m = p.matcher(link);
            if (m.find()) {
                lastPageNum = Integer.valueOf(m.group(1));
                LOG.debug("parse result: " + lastPageNum);
            }
        } catch (NullPointerException npe) {
            //  npe.printStackTrace();
            LOG.info("Propably " + projectName + "commits consists from only one page");
            return 1;
        }
        return lastPageNum;
    }

    public synchronized boolean hasNext() {
        return counter.get() > 0;
    }

    /**
     * @return JsonNode that represents a stargazers list
     */
    public JsonNode next() {
        if (counter.get() > 0) {
            String basicURL = "https://api.github.com/repos/" + projectName + "/" + githubEndpoint.toString().toLowerCase();
            UriComponents page;
            if (since != null) {
                page = UriComponentsBuilder.fromHttpUrl(basicURL)
                        .queryParam("page", counter.getAndDecrement())
                        .queryParam("since", since)
                        .build();
            } else {
                page = UriComponentsBuilder.fromHttpUrl(basicURL)
                        .queryParam("page", counter.getAndDecrement())
                        .build();
            }

            String URL = page.encode().toUriString();
            LOG.debug(URL);
            //sent request
            JsonNode node = restTemplate.getForObject(URL, JsonNode.class);
            LOG.debug(node.toString());
            return node;
        } else throw new IndexOutOfBoundsException("there is no next element");
    }

    /**
     * @param batchSize number of numberOfPages to return
     * @return list of stargazer numberOfPages according to batchSize
     * @throws IndexOutOfBoundsException if there is no elements left to return
     */
    public List<JsonNode> next(int batchSize) throws ExecutionException, InterruptedException {
        if (batchSize > counter.get()) {
            LOG.warn("batch size is bigger then number of elements left, decrease batch size to " + counter);
            batchSize = counter.get();
        }

        List<CompletableFuture<JsonNode>> completableFutures = IntStream.range(0, batchSize)
                .mapToObj(
                        e -> CompletableFuture.supplyAsync(this::next, executor)
                ).collect(Collectors.toList());

        CompletableFuture<List<JsonNode>> result = CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));//compose all in one task
        List<JsonNode> jsonNodes = result.get();//
        LOG.debug("batch completed, counter:" + counter.get());

        return jsonNodes;
    }


}
