package com.ssbusy.core.vote.dao;

import java.io.Serializable;
import java.util.List;

import com.ssbusy.core.vote.domin.Vote;

public interface VoteDao extends Serializable{
	
	Vote create();

	Vote loadVote(Long customerId);
	
	List<Object> countVoteNum();
	
	void save(Vote vote);
	
}
