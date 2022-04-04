package com.epam.rd.java.basic.topic07.task03.db;

import java.util.List;

import com.epam.rd.java.basic.topic07.task03.db.entity.*;


public class DBManager {

	public static synchronized DBManager getInstance() {
		return null;
	}

	public List<User> findAllUsers() throws DBException {
		return null;
	}

	public boolean insertUser(User user) throws DBException {
		return false;
	}

	public boolean deleteUsers(User... users) throws DBException {
		return false;
	}

	public User getUser(String login) throws DBException {
		return null;
	}

	public Team getTeam(String name) throws DBException {
		return null;
	}

	public List<Team> findAllTeams() throws DBException {
		return null;
	}

	public boolean insertTeam(Team team) throws DBException {
		return false;
	}

	public boolean setTeamsForUser(User user, Team... teams) throws DBException {
		return false;
	}

	public List<Team> getUserTeams(User user) throws DBException {
		return null;
	}

	public List<User> getTeamUsers(Team team) throws DBException {
		return null;
	}

	public boolean deleteTeam(Team team) throws DBException {
		return false;
	}

	public boolean updateTeam(Team team) throws DBException {
		return false;
	}

}
