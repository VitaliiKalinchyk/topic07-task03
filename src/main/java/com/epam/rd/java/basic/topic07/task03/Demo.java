package com.epam.rd.java.basic.topic07.task03;

import com.epam.rd.java.basic.topic07.task03.db.*;
import com.epam.rd.java.basic.topic07.task03.db.entity.*;

public class Demo {

	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov, petrov]
		// teams  ==> [teamA teamB teamC]
		// teamA contains the following users: ivanov, petrov 
		
		DBManager dbManager = DBManager.getInstance();

		User userPetrov = dbManager.getUser("petrov");
		System.out.println((dbManager.getUserTeams(userPetrov)));
		// [teamA]

		Team teamA = dbManager.getTeam("teamA");
		Team teamC = dbManager.getTeam("teamC");

		System.out.println((dbManager.getTeamUsers(teamA)));
		// [ivanov, petrov]
		
		// on delete cascade!
		dbManager.deleteTeam(teamA);
		teamC.setName("teamX");
		dbManager.updateTeam(teamC);
		System.out.println((dbManager.findAllTeams()));
		// teams ==> [teamB, teamX]
		
		for (Team team : dbManager.findAllTeams()) {
			dbManager.deleteTeam(team);
		}

		dbManager.insertTeam(Team.createTeam("teamB"));
		System.out.println((dbManager.findAllTeams()));
		// teams ==> [teamB]        
		
		User userIvanov = dbManager.getUser("ivanov");
		System.out.println((dbManager.getUserTeams(userIvanov)));
		// teams ==> []
	}

}
