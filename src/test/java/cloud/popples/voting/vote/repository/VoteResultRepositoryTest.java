package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.VoteItemSum;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql(scripts = {"/unit-test-db-scripts/vote_result.sql",
        "/unit-test-db-scripts/vote_item.sql"})
class VoteResultRepositoryTest {

    @Autowired
    VoteResultRepository resultRepository;

    @Test
    void testCountTag() {
        List<VoteItemSum> voteItemSums = resultRepository.countTagsByVoteId(1L);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(voteItemSums));
    }

}
