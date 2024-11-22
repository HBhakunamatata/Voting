package cloud.popples.voting.vote.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VoteResultForm {

    private String voteId;

    private List<String> voteItems;

    public List<VoteResult> toVoteResults() {
        List<VoteResult> voteResults = new ArrayList<>();
        for (String itemId : voteItems) {
            VoteResult voteResult = VoteResult.builder()
                    .voteId(Long.parseLong(voteId))
                    .itemId(Long.parseLong(itemId))
                    .build();
            voteResults.add(voteResult);
        }
        return voteResults;
    }

}
