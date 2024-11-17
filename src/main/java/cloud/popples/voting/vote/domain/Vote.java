package cloud.popples.voting.vote.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static cloud.popples.voting.utils.DateTimeUtil.DEFAULT_DATETIME_FORMAT;

@Entity
@Table(name = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
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
    private List<VoteItem> voteItems;

}
