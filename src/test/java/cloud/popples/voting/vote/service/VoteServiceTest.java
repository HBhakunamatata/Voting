package cloud.popples.voting.vote.service;

import cloud.popples.voting.vote.domain.VoteItemSum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Test
    void testGetPercentage() {
        List<VoteItemSum> resultSummary = voteService.resultSummary("1");
        Assertions.assertFalse(resultSummary.isEmpty());
    }


    @Test
    void testGetOutdatedVotes() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(today, LocalTime.MAX);
        int updated = voteService.updateOutdatedVotes(start, end);
        Assertions.assertEquals(1, updated);
    }
}
