package cloud.popples.voting.vote.service.impl;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.exception.VoteNotFoundException;
import cloud.popples.voting.vote.form.VoteForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.service.VoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
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
    public int updateOutdatedVotes(LocalDateTime start, LocalDateTime end) {
        return voteRepository.updateStatusByEndTimeBetween(VoteStatus.END, start, end);
    }

}
