package com.ssbusy.core.vote.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import com.ssbusy.core.vote.domin.Vote;
import com.ssbusy.core.vote.domin.VoteImpl;

@Repository("voteDao")
public class VoteDaoImpl implements VoteDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;


	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;


	@Override
	public Vote create() {
		Vote vote = (Vote) entityConfiguration.createEntityInstance(Vote.class.getName());
		vote.setCustomerId(null);
		return vote;
	}


	@Override
	public Vote loadVote(Long customerId) {
		return em.find(VoteImpl.class, customerId);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object> countVoteNum() {
		Query query = em.createQuery("select v.teamId, v.teamName, count(v.customerId) from com.ssbusy.core.vote.domin.Vote v group by v.teamId,v.teamName order by v.teamId");
		return query.getResultList();
	}


	@Override
	public void save(Vote vote) {
		em.persist(vote);
	}


}
