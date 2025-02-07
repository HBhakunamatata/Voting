package cloud.popples.voting.vote.controller;

import cloud.popples.voting.utils.TestUtils;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItem;
import cloud.popples.voting.vote.domain.VoteItemForm;
import cloud.popples.voting.vote.domain.VoteStatus;
import cloud.popples.voting.vote.form.VoteForm;
import cloud.popples.voting.vote.service.VoteService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {VoteController.class})
public class VoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VoteService voteService;

    @Test
    @WithMockUser
    void whenSavedVoteThenReturnCreated() throws Exception {
        // prepare
        String expectSubject = "testSubject";
        LocalDateTime endTime = LocalDateTime.of(2025, 11, 10, 0, 0, 0);

        VoteForm voteForm = new VoteForm();
        voteForm.setVoteSubject(expectSubject);
        voteForm.setEndTime(endTime);

        VoteItemForm voteItemForm = new VoteItemForm();
        voteItemForm.setTag("A");
        voteItemForm.setContent("contentForA");

        voteForm.setVoteItemForms(Lists.newArrayList(voteItemForm));

        // action
        VoteItem voteItem = new VoteItem("A", "contentForA");
        Vote vote = Vote.builder()
                .id(1L)
                .subject(expectSubject)
                .endTime(endTime)
                .startTime(endTime)
                .status(VoteStatus.GOING)
                .voteItems(Lists.newArrayList(voteItem))
                .build();
        when(voteService.saveVote(voteForm)).thenReturn(vote);

        mvc.perform(post("/vote").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(TestUtils.mapToString(voteForm))
        ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..id").value(1));
    }

}
