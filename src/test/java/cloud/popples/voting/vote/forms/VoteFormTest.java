package cloud.popples.voting.vote.forms;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItemForm;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.form.VoteForm;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class VoteFormTest {

    @Test
    public void givenVoteFormThenReturnVote() {
        // prepare
        String expectedSubject = "testSubject";
        String expectedTag = "A";
        String expectedContent = "contentA";

        VoteForm voteForm = new VoteForm();
        voteForm.setVoteSubject(expectedSubject);
        VoteItemForm voteItemForm = new VoteItemForm();
        voteItemForm.setTag(expectedTag);
        voteItemForm.setContent(expectedContent);
        voteForm.setVoteItemForms(Lists.newArrayList(voteItemForm));

        LocalDateTime endTime = LocalDateTime.of(2024, 11, 10, 0,0,0);
        voteForm.setEndTime(endTime);

        // action
        Vote vote = voteForm.toVote();

        // assert
        assertAll(
                () -> assertEquals(vote.getSubject(), expectedSubject),
                () -> assertEquals(vote.getEndTime(), endTime),
                () -> assertNotNull(vote.getStartTime()),
                () -> assertNotNull(vote.getVoteItems()),
                () -> assertEquals(vote.getStatus(), VoteStatus.GOING)
        );
    }

}
