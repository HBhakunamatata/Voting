package cloud.popples.voting.vote.service.impl;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.exception.OperationNotAllowException;
import cloud.popples.voting.vote.exception.VoteNotFoundException;
import cloud.popples.voting.vote.form.VoteForm;
import cloud.popples.voting.vote.form.VoteResultForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.VoteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    public Page<Vote> conditionQuery(int pageNo, int pageSize, String queryStr) {
        final String queryWord = queryWord(queryStr);
        final PageRequest pageRequest = votePageRequest(pageNo, pageSize);
        return voteRepository
                .findBySubjectContainsIgnoreCase(queryWord, pageRequest);
    }

    private String queryWord(String query) {
        return StringUtils.isBlank(query) ? "" : query.trim();
    }

    /**
     * get votes sorted by endTime descending and status ascending
     * @return Sort
     */
    private Sort voteSort() {
        final Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        return typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
    }

    /**
     * create pageRequest with sort and page info
     * @param pageNo
     * @param pageSize
     * @return pageRequest
     */
    private PageRequest votePageRequest(int pageNo, int pageSize) {
        final Sort sort = voteSort();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    @Override
    public Vote getVoteDetails(Long voteId) {
        return voteRepository.findById(voteId).orElseThrow(VoteNotFoundException::new);
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
        return resultRepository.getByVoteIdAndCreator(
                Long.parseLong(voteId), userInfo.getId());
    }

    @Override
    public List<VoteItemSum> resultSummary(String voteId) {
        return resultRepository.countTagsByVoteId(Long.parseLong(voteId));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public int updateOutdatedVotes(LocalDateTime start, LocalDateTime end) {
        return voteRepository.updateStatusByEndTimeBetween(VoteStatus.END, start, end);
    }

}
