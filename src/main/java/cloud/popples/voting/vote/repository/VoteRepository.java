package cloud.popples.voting.vote.repository;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Page<Vote> findBySubjectContainsIgnoreCase(String voteSubject, Pageable pageable);

//    List<Vote> findByStatusNotAndEndTimeBetween(VoteStatus status, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query(value = "update Vote vote set vote.status = 2 where vote.status != ?1 and (vote.endTime between ?2 and ?3)")
    int updateStatusByEndTimeBetween(VoteStatus status, LocalDateTime start, LocalDateTime end);

}
