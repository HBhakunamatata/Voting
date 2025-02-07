package cloud.popples.voting.vote.service;

import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteResultRepository voteResultRepository;

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
        Assertions.assertEquals(0, updated);
    }
}
