package io.zipcoder.tc_spring_poll_application.controller;

import dtos.OptionCount;
import dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.domain.Option;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class ComputeResultController {
    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);

        //Implement algorithm to count votes
        Map<Long, Integer> countsMap = new HashMap();
        Long optionId;
        for (Vote vote : allVotes) {
            optionId = vote.getOption().getId();
            int updatedCount;
            if (!countsMap.containsKey(optionId))
                countsMap.put(optionId, 1);
            else {
                updatedCount = countsMap.get(optionId) + 1;
                countsMap.put(optionId, updatedCount);
            }
        }

        Set<OptionCount> optionCounts = new HashSet<>();
        for (Long id : countsMap.keySet()) {
            optionCounts.add(new OptionCount(id, countsMap.get(id)));
        }
        voteResult.setResults(optionCounts);
        voteResult.setTotalVotes(voteResult.getTotalVotes());

        return new ResponseEntity<>(voteResult, HttpStatus.OK);
    }

}
