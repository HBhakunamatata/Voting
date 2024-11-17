package cloud.popples.voting.vote.controller;

import cloud.popples.voting.common.PageResult;
import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.vote.domain.Vote;
import cloud.popples.voting.vote.domain.VoteItemSum;
import cloud.popples.voting.vote.domain.VoteResult;
import cloud.popples.voting.vote.domain.VoteResultForm;
import cloud.popples.voting.vote.exception.VoteNotFoundException;
import cloud.popples.voting.vote.form.VoteForm;
import cloud.popples.voting.vote.service.VoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/")
@Controller
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/vote")
    @ResponseBody
    public Vote save(@Valid @RequestBody VoteForm voteForm) throws JsonProcessingException {
        Vote savedVote = voteService.saveVote(voteForm);
        return savedVote;
    }

    @GetMapping("/votes")
    @ResponseBody
    public PageResult<? extends Vote> conditionQuery(@RequestParam("pageNo") int pageNo,
                                                     @RequestParam("pageSize") int pageSize,
                                                     @RequestParam("queryWord") String queryWord)
    {
        Page<Vote> votes = voteService.conditionQuery(pageNo-1, pageSize, queryWord);
        return PageResult.success(votes);
    }

    @GetMapping("/vote/{voteId}")
    public String voteDetails(@PathVariable("voteId") Long voteId, Model model) {
        Vote voteDetails = voteService.getVoteDetails(voteId);
        if (voteDetails == null) {
            throw new VoteNotFoundException();
        }
        model.addAttribute("voteData", voteDetails);
        return "vote-details";
    }


    @PatchMapping("/vote/result")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<VoteResult> saveVoteResults(
            @RequestBody VoteResultForm voteResultForm,
            @AuthenticationPrincipal UserInfo userInfo) {
        List<VoteResult> results = voteService.saveVoteResults(voteResultForm, userInfo);
        return results;
    }


    @GetMapping("/vote/{voteId}/result")
    @ResponseBody
    public List<VoteResult> getUserVoteResults(
            @PathVariable String voteId, @AuthenticationPrincipal UserInfo userInfo) {
        List<VoteResult> userVoteResult = voteService.getUserVoteResult(voteId, userInfo);
        return userVoteResult;
    }


    @GetMapping("/vote/{voteId}/summary")
    @ResponseBody
    public List<VoteItemSum> resultSummery(@PathVariable String voteId) {
        List<VoteItemSum> resultSummary = voteService.resultSummary(voteId);
        return resultSummary;
    }


}
