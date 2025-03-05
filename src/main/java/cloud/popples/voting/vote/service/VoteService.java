package cloud.popples.voting.vote.service;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.form.VoteForm;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface VoteService {

    Vote saveVote(VoteForm voteForm);

    Page<Vote> conditionQuery(int pageNo, int pageSize, String queryWord);

    Vote getVoteDetails(Long voteId);

    int updateOutdatedVotes(LocalDateTime start, LocalDateTime end);
}
