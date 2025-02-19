package cloud.popples.voting.vote.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cloud.popples.voting.utils.DateTimeUtil.DEFAULT_DATETIME_FORMAT;

@Entity
@Table(name = "vote")
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class Vote extends BaseVoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @JsonFormat(pattern = DEFAULT_DATETIME_FORMAT, timezone = "GTM+8")
    private LocalDateTime endTime;

    @JsonFormat(pattern = DEFAULT_DATETIME_FORMAT, timezone = "GTM+8")
    private LocalDateTime startTime;

    @Enumerated(EnumType.ORDINAL)
    private VoteStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vote_id", foreignKey = @ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
    @Setter(AccessLevel.PRIVATE)
    private List<VoteItem> voteItems;

    public List<VoteItem> getVoteItems() {
        return Collections.unmodifiableList(voteItems);
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
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id)
                && Objects.equals(subject, vote.subject)
                && Objects.equals(endTime, vote.endTime)
                && Objects.equals(startTime, vote.startTime)
                && status == vote.status
                && Objects.equals(voteItems, vote.voteItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, subject, endTime, startTime, status, voteItems);
    }

    public boolean notEnd() {
        final LocalDateTime now = LocalDateTime.now();
        return endTime.isAfter(now) && status != VoteStatus.END;
    }
}
