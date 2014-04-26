package com.ssbusy.core.vote.service;

import java.io.Serializable;
import java.util.List;

import com.ssbusy.core.vote.domin.Vote;

public interface VoteService extends Serializable {
	
	Vote loadVote(Long customerId);
	
	List<Object> countVoteNum();
	
	Vote persist(Long customerId,int teamId,String teamName);
}
