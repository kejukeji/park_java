package com.ssbusy.controller.vote;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssbusy.core.account.domain.MyCustomer;
import com.ssbusy.core.vote.domin.TeamForm;
import com.ssbusy.core.vote.domin.Vote;
import com.ssbusy.core.vote.service.VoteService;

@Controller
public class VoteController {

	@Resource(name = "voteService")
	protected VoteService voteService;
	List<TeamForm> teams = new ArrayList<TeamForm>(9);

	private void readyTeams() { 
		if (teams.isEmpty()) {
			teams.add(new TeamForm(0, "飞定",null));
			teams.add(new TeamForm(1, "宣宣",null));
			teams.add(new TeamForm(2, "和谐号",null));
			teams.add(new TeamForm(3, "女希氏",null));
			teams.add(new TeamForm(4, "下一个奇迹",null));
			teams.add(new TeamForm(5, "H.A.",null));
			teams.add(new TeamForm(6, "BE YOUNG",null));
			teams.add(new TeamForm(7, "chanllenger",null));
			teams.add(new TeamForm(8, "Practitioners",null));
		} 
	}

	@RequestMapping("/activity/votes")
	public void voteView(HttpServletRequest request,Model model){
		//显示页面方法
		readyTeams();
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		Vote vote = voteService.loadVote(myCustomer.getId());
		if(vote==null){
			model.addAttribute("teams", teams);
		}else{
			List<Object> os = voteService.countVoteNum();
			List<TeamForm> teamsVote = new ArrayList<TeamForm>(9);
			double maxVote = 0;
			for(int i=0;i<os.size();i++){
 				Integer teamId = (Integer) Array.get(os.get(i), 0);
				String teamName = (String) Array.get(os.get(i), 1);
				Long teamVote = (Long) Array.get(os.get(i), 2);
				//maxVote = maxVote+ teamVote.doubleValue();
				if(maxVote<teamVote.doubleValue())
					maxVote = teamVote.doubleValue();
				teamsVote.add(new TeamForm(teamId,teamName,teamVote));
			}
			model.addAttribute("totalVote", maxVote);
			model.addAttribute("teams", teamsVote);
			model.addAttribute("alleadyVote","alleadyVote");
		}
	}

	@ResponseBody
	@RequestMapping("/activity/votes/submit")
	public Map<String,Object> voteSubmit(@RequestParam("teamId") int teamId) {
		readyTeams();
		TeamForm teamForm = null;
		for(TeamForm tf :teams){
			if(tf.getTeamId()==teamId){
				teamForm = tf;
				break;
			}
		}
		MyCustomer myCustomer = (MyCustomer) CustomerState.getCustomer();
		Map<String,Object> ret = new HashMap<String, Object>(1); 
		if(myCustomer.isRegistered()){
			if(teamForm!=null){	
				Vote vote = voteService.loadVote(myCustomer.getId());
				if(vote==null){
					voteService.persist(myCustomer.getId(), teamForm.getTeamId(), teamForm.getTeamName());
					ret.put("info", "success");
				}
			}
		}else{
			ret.put("info", "failed");
		}
		return ret;
	}
}
