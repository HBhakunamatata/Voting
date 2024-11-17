package cloud.popples.voting.vote.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.domain.VoteResultForm;
import cloud.popples.voting.vote.form.VoteForm;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {

    Vote saveVote(VoteForm voteForm);

    Page<Vote> conditionQuery(int pageNo, int pageSize, String queryWord);

    Vote getVoteDetails(Long voteId);

    List<VoteResult> saveVoteResults(VoteResultForm voteResultForm, UserInfo userInfo);

    List<VoteResult> getUserVoteResult(String voteId, UserInfo userInfo);

    List<VoteItemSum> resultSummary(String voteId);

    int updateOutdatedVotes(LocalDateTime start, LocalDateTime end);
}
