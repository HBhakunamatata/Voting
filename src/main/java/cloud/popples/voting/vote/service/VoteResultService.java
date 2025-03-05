package cloud.popples.voting.vote.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.form.VoteResultForm;

import java.util.List;

public interface VoteResultService {

    List<VoteResult> saveVoteResults(VoteResultForm voteResultForm, UserInfo userInfo);

    List<VoteResult> getUserVoteResult(String voteId, UserInfo userInfo);

    List<VoteItemSum> resultSummary(String voteId);

}
