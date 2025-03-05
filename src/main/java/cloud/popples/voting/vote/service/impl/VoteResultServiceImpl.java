package cloud.popples.voting.vote.service.impl;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.exception.OperationNotAllowException;
import cloud.popples.voting.vote.form.VoteResultForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.VoteResultService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VoteResultServiceImpl implements VoteResultService {

    private final VoteRepository voteRepository;

    private final VoteResultRepository resultRepository;

    @Autowired
    public VoteResultServiceImpl(VoteRepository voteRepository, VoteResultRepository resultRepository) {
        this.voteRepository = voteRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<VoteResult> saveVoteResults(VoteResultForm voteResultForm, UserInfo userInfo) {
        final long voteId = Long.parseLong(voteResultForm.getVoteId());
        if (!isVoteOpen(voteId, userInfo)) {
            throw new OperationNotAllowException();
        }
        List<VoteResult> voteResults = voteResultForm.toVoteResults();
        return resultRepository.saveAll(voteResults);
    }

    private boolean checkUserVoted(long voteId, UserInfo userInfo) {
        List<VoteResult> userVoteResult = getUserVoteResult("" + voteId, userInfo);
        return CollectionUtils.isNotEmpty(userVoteResult);
    }

    private boolean checkVoteNotEnd(long voteId) {
        final Vote vote = voteRepository.getReferenceById(voteId);
        return vote.notEnd();
    }

    private boolean isVoteOpen(long voteId, UserInfo userInfo) {
        return !checkUserVoted(voteId, userInfo) && checkVoteNotEnd(voteId);
    }

    @Override
    public List<VoteResult> getUserVoteResult(String voteId, UserInfo userInfo) {
        return resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId());
    }

    @Override
    public List<VoteItemSum> resultSummary(String voteId) {
        return resultRepository.countTagsByVoteId(Long.parseLong(voteId));
    }
}
