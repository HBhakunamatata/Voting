package cloud.popples.voting.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "vote_result")
public class VoteResult extends BaseVoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "none", foreignKey = @ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
    private Long voteId;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "none", foreignKey = @ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
    private Long itemId;

}
