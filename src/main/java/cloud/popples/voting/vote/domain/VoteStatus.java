package cloud.popples.voting.vote.domain;

public enum VoteStatus {

    NOT_START(0),
    GOING(1),
    END(2)

    ;

    final int value;

    VoteStatus(int value) {
        this.value = value;
    }
}
