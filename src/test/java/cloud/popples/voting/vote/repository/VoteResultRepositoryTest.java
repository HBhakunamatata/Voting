package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.VoteItemSum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class VoteResultRepositoryTest {

    @Autowired
    VoteResultRepository resultRepository;

    @Test
    void testCountTag() {
        List<VoteItemSum> voteItemSums = resultRepository.countTagsByVoteId(1L);
        Assert.notEmpty(voteItemSums);
        for (VoteItemSum voteItemSum : voteItemSums) {
            System.out.println(voteItemSum.getTag() + ":" + voteItemSum.getSummary());
        }
    }

}
