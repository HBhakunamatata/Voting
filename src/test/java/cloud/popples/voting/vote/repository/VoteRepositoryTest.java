package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/unit-test-db-scripts/vote.sql"})
class VoteRepositoryTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void whenPageVotesBySubjectNameThenReturnMatchers() {
        String subjectName = "test";
        Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 5, sort);
        Page<Vote> resultVotes = voteRepository.findBySubjectContainsIgnoreCase(subjectName, pageRequest);
        assertAll(
                () -> assertTrue(CollectionUtils.isNotEmpty(resultVotes.getContent()))
        );
    }

    @Test
    void whenPageVotesWithoutSubjectNameThenReturnAll() {
        Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 5, sort);
        Page<Vote> resultVotes = voteRepository.findBySubjectContainsIgnoreCase("", pageRequest);
        assertAll(
                () -> assertTrue(CollectionUtils.isNotEmpty(resultVotes.getContent()))
        );
    }

    @Test
    void whenFindNotFinishedVotesBetweenTimesThenReturn() {
        LocalDateTime start = LocalDateTime.of(2024, 11, 10, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 11, 10, 23, 59, 59);

        int i = voteRepository.updateStatusByEndTimeBetween(VoteStatus.END, start, end);
        Assertions.assertEquals(2, i);
    }
}
