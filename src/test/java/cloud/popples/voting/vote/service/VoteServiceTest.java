package cloud.popples.voting.vote.service;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.impl.VoteServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteResultRepository resultRepository;

    @Test
    void whenPageQueryVoteThenReturnSatisfiedVotes() {
        final Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        List<Vote> votes = Instancio.ofList(Vote.class).size(2).create();
        PageImpl<Vote> expect = new PageImpl<>(votes, pageRequest, votes.size());

        String queryWord = "test";
        when(voteRepository.findBySubjectContainsIgnoreCase(queryWord, pageRequest)).thenReturn(expect);

        Page<Vote> result = voteService.conditionQuery(0, 10, queryWord);

        assertEquals(expect, result);
    }

    @Test
    void whenPageQueryVoteWithEmptyQueryWordThenReturnAll() {
        final Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        List<Vote> votes = Instancio.ofList(Vote.class).size(2).create();
        PageImpl<Vote> expect = new PageImpl<>(votes, pageRequest, votes.size());

        when(voteRepository.findBySubjectContainsIgnoreCase("", pageRequest)).thenReturn(expect);

        Page<Vote> result = voteService.conditionQuery(0, 10, null);

        assertEquals(expect, result);
    }



}
