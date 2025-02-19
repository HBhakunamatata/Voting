package cloud.popples.voting.vote.form;

import cloud.popples.voting.vote.domain.VoteResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VoteResultForm {

    private String voteId;

    private List<String> voteItems;

    public List<VoteResult> toVoteResults() {
//        List<VoteResult> voteResults = new ArrayList<>();
//        for (String itemId : voteItems) {
//            VoteResult voteResult = VoteResult.builder()
//                    .voteId(Long.parseLong(voteId))
//                    .itemId(Long.parseLong(itemId))
//                    .build();
//            voteResults.add(voteResult);
//        }
//        return voteResults;
        return toVoteResults2();
    }

    private List<VoteResult> toVoteResults2() {
        return voteItems.stream().map(this::toVoteResult).collect(Collectors.toList());
    }

    private VoteResult toVoteResult(String itemId) {
        return VoteResult.builder()
                .voteId(Long.parseLong(voteId))
                .itemId(Long.parseLong(itemId))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VoteResultForm that = (VoteResultForm) o;
        return Objects.equals(voteId, that.voteId)
                && Objects.equals(voteItems, that.voteItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteId, voteItems);
    }
}
