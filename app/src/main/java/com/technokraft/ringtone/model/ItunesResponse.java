package com.technokraft.ringtone.model;

import java.util.List;

public class ItunesResponse {
    private int resultCount;
    private List<Song> results;

    public ItunesResponse() {
    }

    public ItunesResponse(int resultCount, List<Song> results) {
        this.resultCount = resultCount;
        this.results = results;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<Song> getResults() {
        return results;
    }

    public void setResults(List<Song> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ItunesResponse{" +
                "resultCount=" + resultCount +
                ", results=" + results +
                '}';
    }
}
