package cloud.popples.voting.vote.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.exception.OperationNotAllowException;
import cloud.popples.voting.vote.form.VoteResultForm;
import cloud.popples.voting.vote.repository.VoteRepository;
import cloud.popples.voting.vote.repository.VoteResultRepository;
import cloud.popples.voting.vote.service.impl.VoteResultServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteResultServiceTest {

    @InjectMocks
    private VoteResultServiceImpl voteResultService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteResultRepository resultRepository;

    @Test
    void whenUnvotedUserSaveResultsThenReturn() {
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

        Vote unfinishedVote = Instancio.of(Vote.class)
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
                .thenReturn(unfinishedVote);
        when(resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId()))
                .thenReturn(Collections.emptyList());
        lenient().when(resultRepository.saveAll(unsavedResults))
                .thenReturn(expectedResults);

        List<VoteResult> voteResults = voteResultService.saveVoteResults(voteResultForm, userInfo);
        assertTrue(CollectionUtils.isNotEmpty(voteResults));
    }

    @Test
    void whenVotedUserSaveResultThenThrowNotAllowedException() {
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

        List<VoteResult> userVotedResults = Instancio.ofList(VoteResult.class)
                .size(1)
                .set(field(VoteResult::getVoteId), Long.parseLong(voteId))
                .set(field(VoteResult::getCreator), userInfo.getId())
                .create();

        when(resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId()))
                .thenReturn(userVotedResults);

        assertThrows(OperationNotAllowException.class, () -> voteResultService.saveVoteResults(voteResultForm, userInfo));
    }

    @Test
    void whenSaveResultForFinishedVoteThenThrowNotAllowedException() {
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

        Vote finishedVote = Instancio.of(Vote.class)
                .set(field(Vote::getId), Long.parseLong(voteId))
                .set(field(Vote::getEndTime), LocalDateTime.of(1099, 1, 1, 0,0,0))
                .create();

        when(voteRepository.getReferenceById(Long.parseLong(voteId)))
                .thenReturn(finishedVote);
        when(resultRepository.getByVoteIdAndCreator(Long.parseLong(voteId), userInfo.getId()))
                .thenReturn(Collections.emptyList());

        assertThrows(OperationNotAllowException.class, () -> voteResultService.saveVoteResults(voteResultForm, userInfo));
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

        List<VoteResult> userVoteResult = voteResultService.getUserVoteResult(voteId, userInfo);
        assertTrue(CollectionUtils.isNotEmpty(userVoteResult));
    }

}
