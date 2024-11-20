package cloud.popples.voting.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vote_item")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class VoteItem extends BaseVoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String tag;

    private String content;


    public VoteItem(String tag, String content) {
        this.tag = tag;
        this.content = content;
    }

}
