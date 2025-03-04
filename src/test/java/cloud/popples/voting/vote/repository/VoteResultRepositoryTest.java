package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = {"/unit-test-db-scripts/vote_result.sql",
        "/unit-test-db-scripts/vote_item.sql"
})
class VoteResultRepositoryTest {

    @Autowired
    VoteResultRepository resultRepository;

    @Test
    void whenCountTagForVoteThenReturnSumInfo() {
        List<VoteItemSum> voteItemSums = resultRepository.countTagsByVoteId(1L);
        assertAll(
                () -> assertEquals(3, voteItemSums.size()),
                () -> assertEquals(1, voteItemSums.get(0).getSummary())
        );
    }

    @Test
    void giveVoteIdAndCreatorIdThenReturnResultList() {
        Long voteId = 1L;
        Long creatorId = 2L;
        List<VoteResult> results = resultRepository.getByVoteIdAndCreator(voteId, creatorId);
        assertEquals(1, results.size());
    }

    @Test
    void giveUnvotedUserThenReturnEmptyList() {
        Long voteId = 1L;
        Long creatorId = 1L;
        List<VoteResult> results = resultRepository.getByVoteIdAndCreator(voteId, creatorId);
        assertEquals(0, results.size());
    }

}
