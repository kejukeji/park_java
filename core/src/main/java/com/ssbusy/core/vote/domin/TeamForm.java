package com.ssbusy.core.vote.domin;

import java.io.Serializable;

public class TeamForm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int teamId;
	public TeamForm(){
		super();
	}
	public TeamForm(int teamId, String teamName,Long teamVote) {
		super();
		this.teamId = teamId;
		this.teamName = teamName;
		this.teamVote = teamVote;
	}
	private String teamName;
	
	private Long teamVote;
	public Long getTeamVote() {
		return teamVote;
	}
	public void setTeamVote(Long teamVote) {
		this.teamVote = teamVote;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
