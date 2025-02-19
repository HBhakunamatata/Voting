package cloud.popples.voting.vote.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "vote_result")
public class VoteResult extends BaseVoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    final private Long voteId;

    final private Long itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        VoteResult that = (VoteResult) o;
        return Objects.equals(id, that.id)
                && Objects.equals(voteId, that.voteId)
                && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, voteId, itemId);
    }

}
