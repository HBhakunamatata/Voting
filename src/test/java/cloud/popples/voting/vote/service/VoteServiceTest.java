package cloud.popples.voting.vote.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.form.VoteResultForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.impl.VoteServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteResultRepository resultRepository;

    @Test
    void whenPageQueryVoteThenReturnSatisfiedVotes() {
        final Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        List<Vote> votes = Instancio.ofList(Vote.class).size(2).create();
        PageImpl<Vote> expect = new PageImpl<>(votes, pageRequest, votes.size());

        String queryWord = "test";
        when(voteRepository.findBySubjectContainsIgnoreCase(queryWord, pageRequest)).thenReturn(expect);

        Page<Vote> result = voteService.conditionQuery(0, 10, queryWord);

        assertEquals(result, expect);
    }

    @Test
    void whenPageQueryVoteWithEmptyQueryWordThenReturnAll() {
        final Sort.TypedSort<Vote> typedSort = Sort.sort(Vote.class);
        Sort sort = typedSort.by(Vote::getEndTime).descending()
                .and(typedSort.by(Vote::getStatus).ascending());
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        List<Vote> votes = Instancio.ofList(Vote.class).size(2).create();
        PageImpl<Vote> expect = new PageImpl<>(votes, pageRequest, votes.size());

        when(voteRepository.findBySubjectContainsIgnoreCase("", pageRequest)).thenReturn(expect);

        Page<Vote> result = voteService.conditionQuery(0, 10, null);

        assertEquals(result, expect);
    }

    @Test
    void whenSaveVoteResultThenReturnSavedVoteResult() {
        String voteId = "1";
        String itemId_2 = "2";
        String itemId_3 = "3";
        VoteResultForm voteResultForm = new VoteResultForm();
        voteResultForm.setVoteId(voteId);
        voteResultForm.setVoteItems(Lists.newArrayList(itemId_2, itemId_3));

        UserRole userRole = new UserRole("ROLE_USER");
        UserInfo userInfo = Instancio.of(UserInfo.class)
                .set(field(UserInfo::getId), 1L)
                .set(field(UserInfo::getAuthorities), Sets.newLinkedHashSet(userRole))
                .create();

        Vote expectedVote = Instancio.of(Vote.class)
                .set(field(Vote::getId), Long.parseLong(voteId))
                .set(field(Vote::getStatus), VoteStatus.GOING)
                .set(field(Vote::getEndTime), LocalDateTime.of(2099, 1, 1, 0,0,0))
                .create();
        List<VoteResult> unsavedResults = voteResultForm.toVoteResults();
        List<VoteResult> expectedResults = Instancio.ofList(VoteResult.class)
                .size(2)
                .set(field(VoteResult::getVoteId), Long.parseLong(voteId))
                .create();

        when(voteRepository.getReferenceById(Long.parseLong(voteId)))
                .thenReturn(expectedVote);
        when(resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId()))
                .thenReturn(Collections.emptyList());
        lenient().when(resultRepository.saveAll(unsavedResults))
                .thenReturn(expectedResults);

        List<VoteResult> voteResults = voteService.saveVoteResults(voteResultForm, userInfo);
        assertTrue(CollectionUtils.isNotEmpty(voteResults));
    }

    @Test
    void whenGetUserVoteResultThenReturnSavedVoteResult() {
        String voteId = "1";
        UserInfo userInfo = Instancio.of(UserInfo.class)
                .set(field(UserInfo::getId), 1L)
                .create();
        List<VoteResult> expectedResults = Instancio.ofList(VoteResult.class).create();

        when(resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId()))
                .thenReturn(expectedResults);

        List<VoteResult> userVoteResult = voteService.getUserVoteResult(voteId, userInfo);
        assertTrue(CollectionUtils.isNotEmpty(userVoteResult));
    }

}
