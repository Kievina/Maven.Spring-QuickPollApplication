package io.zipcoder.tc_spring_poll_application.controller;

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
        List<Vote> votes = new ArrayList<>();
        allVotes.forEach(votes::add);
        List<Option> optionList = new ArrayList<>();
        for (Vote vote : votes) {
            optionList.add(vote.getOption());
        }
        Map<Long, Integer> optionCount = new HashMap();
        for (Vote vote : allVotes) {
            if (!optionCount.containsKey(vote.getOption().getId()))
                optionCount.put(vote.getOption().getId(), 1);
            else
                optionCount.put(vote.getOption().getId(), optionCount.get(vote.getOption().getId()) + 1);
        }
        optionList.stream().collect(Collectors.groupingBy(Option::getId, Collectors.counting()));
        return new ResponseEntity<>(voteResult, HttpStatus.OK);
    }

}
