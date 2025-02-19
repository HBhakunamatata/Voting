package cloud.popples.voting.vote.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VoteItemForm {

    @NotNull
    private String tag;

    @NotNull
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VoteItemForm that = (VoteItemForm) o;
        return Objects.equals(tag, that.tag) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, content);
    }
}