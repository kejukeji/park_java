package com.ssbusy.core.vote.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssbusy.core.vote.dao.VoteDao;
import com.ssbusy.core.vote.domin.Vote;

@Service("voteService")
public class VoteServiceImpl implements VoteService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(name="voteDao")
	protected VoteDao voteDao;
	@Override
	public Vote loadVote(Long customerId) {
		return voteDao.loadVote(customerId);
	}

	@Override
	public List<Object> countVoteNum() {
		return voteDao.countVoteNum();
	}

	
	@Override
	@Transactional("blTransactionManager")
	public Vote persist(Long customerId,int teamId,String teamName) {
		Vote vote = voteDao.create();
		vote.setCustomerId(customerId);
		vote.setTeamId(teamId);
		vote.setTeamName(teamName);
		voteDao.save(vote);
		return vote;
	}

}
