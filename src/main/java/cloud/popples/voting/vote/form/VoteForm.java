package cloud.popples.voting.vote.form;

import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItem;
import cloud.popples.voting.vote.domain.VoteItemForm;
import cloud.popples.voting.vote.domain.VoteStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cloud.popples.voting.utils.DateTimeUtil.DEFAULT_DATETIME_FORMAT;

@Data
@NoArgsConstructor
public class VoteForm {

    @NotNull
    private String voteSubject;

    @NotNull
    @Size(min = 1)
    private List<VoteItemForm> voteItemForms;

    @NotNull
    @Future
    @JsonFormat(pattern = DEFAULT_DATETIME_FORMAT, timezone = "GTM+8")
    private LocalDateTime endTime;

    public Vote toVote() {
        List<VoteItem> voteItems = this.voteItemForms.stream()
                .map(itemForm -> new VoteItem(itemForm.getTag(), itemForm.getContent()))
                .collect(Collectors.toList());

        Vote vote = Vote.builder()
                .subject(this.voteSubject)
                .voteItems(voteItems)
                .status(VoteStatus.GOING)
                .startTime(LocalDateTime.now())
                .endTime(endTime)
                .build();

        return vote;
    }

}
