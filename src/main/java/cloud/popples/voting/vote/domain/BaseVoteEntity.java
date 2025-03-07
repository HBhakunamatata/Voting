package cloud.popples.voting.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class BaseVoteEntity {

    @CreatedDate
    protected LocalDateTime createTime;

    @LastModifiedDate
    protected LocalDateTime updateTime;

    @CreatedBy
    public Long creator;

    @LastModifiedBy
    protected Long updater;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseVoteEntity that = (BaseVoteEntity) o;
        return Objects.equals(createTime, that.createTime)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(creator, that.creator)
                && Objects.equals(updater, that.updater);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, updateTime, creator, updater);
    }
}
