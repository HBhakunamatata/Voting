package cloud.popples.voting.vote.service.impl;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.*;
import cloud.popples.voting.vote.exception.OperationNotAllowException;
import cloud.popples.voting.vote.form.VoteForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.VoteService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteResultRepository resultRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository,
                           VoteResultRepository resultRepository) {
        this.voteRepository = voteRepository;
        this.resultRepository = resultRepository;
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Vote saveVote(VoteForm voteForm) {
        Vote vote = voteForm.toVote();
        return voteRepository.save(vote);
    }

    @Override
    public Page<Vote> conditionQuery(int pageNo, int pageSize, String queryWord) {
        Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        Page<Vote> votes = voteRepository
                .findBySubjectContainsIgnoreCase(queryWord, pageRequest);
        return votes;
    }

    @Override
    public Vote getVoteDetails(Long voteId) {
        return voteRepository.findById(voteId).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<VoteResult> saveVoteResults(VoteResultForm voteResultForm, UserInfo userInfo) {
        long voteId = Long.parseLong(voteResultForm.getVoteId());
        Vote vote = voteRepository.getReferenceById(voteId);

        LocalDateTime now = LocalDateTime.now();
        if (vote.getEndTime().isBefore(now) || vote.getStatus().equals(VoteStatus.END)) {
            throw new OperationNotAllowException();
        }

        List<VoteResult> voteResult = getUserVoteResult(voteResultForm.getVoteId(), userInfo);
        if (CollectionUtils.isNotEmpty(voteResult)) {
            throw new OperationNotAllowException();
        }
        List<VoteResult> voteResults = voteResultForm.toVoteResults();
        return resultRepository.saveAll(voteResults);
    }

    @Override
    public List<VoteResult> getUserVoteResult(String voteId, UserInfo userInfo) {
        List<VoteResult> voteResults = resultRepository.getByVoteIdAndCreator(
                Long.parseLong(voteId), userInfo.getId());
        return voteResults;
    }

    @Override
    public List<VoteItemSum> resultSummary(String voteId) {
        List<VoteItemSum> summaryList =
                resultRepository.countTagsByVoteId(Long.parseLong(voteId));
        return summaryList;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public int updateOutdatedVotes(LocalDateTime start, LocalDateTime end) {
        return voteRepository.updateStatusByEndTimeBetween(VoteStatus.END, start, end);
    }


}
