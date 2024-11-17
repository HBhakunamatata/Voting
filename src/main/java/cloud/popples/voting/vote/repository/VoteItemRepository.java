package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
}
