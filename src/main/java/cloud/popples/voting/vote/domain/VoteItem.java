package cloud.popples.voting.vote.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "vote_item")
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class VoteItem extends BaseVoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    final private String tag;

    final private String content;


    public VoteItem(String tag, String content) {
        this.tag = tag;
        this.content = content;
    }

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
        VoteItem voteItem = (VoteItem) o;
        return Objects.equals(itemId, voteItem.itemId)
                && Objects.equals(tag, voteItem.tag)
                && Objects.equals(content, voteItem.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), itemId, tag, content);
    }
}
