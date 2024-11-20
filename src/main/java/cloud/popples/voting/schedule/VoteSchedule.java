package cloud.popples.voting.schedule;

import cloud.popples.voting.vote.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
public class VoteSchedule {

    private final VoteService voteService;

    @Autowired
    public VoteSchedule(VoteService voteService) {
        this.voteService = voteService;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void autoCloseTenMinutes() {
        log.debug("start autoCloseTenMinutes");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesAgo = now.minusMinutes(10L).withSecond(0).withNano(0);
        int updated = voteService.updateOutdatedVotes(tenMinutesAgo, now);
        log.info("autoCloseTenMinutes updated: {}", updated);
        log.debug("end autoCloseTenMinutes");
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void autoCloseToday() {
        log.debug("start autoCloseToday");
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(today, LocalTime.MAX);
        int updated = voteService.updateOutdatedVotes(todayStart, todayEnd);
        log.info("autoCloseToday updated: {}", updated);
        log.debug("end autoCloseToday");
    }

}
