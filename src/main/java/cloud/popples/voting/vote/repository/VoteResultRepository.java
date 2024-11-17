package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteResultRepository extends JpaRepository<VoteResult, Long> {

    List<VoteResult> getByVoteIdAndCreator(Long voteId, Long creator);

    @Query(value = "SELECT vi.tag as tag, count(vr.item_id) as summary from vote_item vi " +
            "left join vote_result vr " +
            "on vr.item_id = vi.item_id " +
            "where vi.vote_id = ?1 " +
            "GROUP BY vi.tag, vr.item_id " +
            "ORDER BY vi.tag;", nativeQuery = true)
    List<VoteItemSum> countTagsByVoteId(Long voteId);

}
