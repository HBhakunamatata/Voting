package cloud.popples.voting.vote.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class VoteItemForm {

    @NotNull
    private String tag;

    @NotNull
    private String content;
}