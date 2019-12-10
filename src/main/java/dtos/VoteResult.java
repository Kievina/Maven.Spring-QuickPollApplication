package dtos;

import java.util.Collection;

public class VoteResult {
    private int totalVotes;
    private Collection<OptionCount> results;

    public VoteResult() {
    }

    public int getTotalVotes() {
        int count = 0;
        for (OptionCount option : results) {
            count = count + option.getCount();
        }
        totalVotes = count;
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Collection<OptionCount> getResults() {
        return results;
    }

    public void setResults(Collection<OptionCount> results) {
        this.results = results;
    }
}
