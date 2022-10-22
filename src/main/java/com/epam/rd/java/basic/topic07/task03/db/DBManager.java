package com.epam.rd.java.basic.topic07.task03.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task03.db.entity.*;


public class DBManager {

	private static DBManager dbManager;

	private enum DBQueries {
		INSERT_USER("INSERT INTO users (login) VALUES (?)"),
		INSERT_TEAM("INSERT INTO teams (name) VALUES (?)"),
		INSERT_TEAMS_FOR_USER("INSERT INTO users_teams (user_id, team_id) VALUES (?, ?)"),
		FIND_ALL_USERS("SELECT * FROM users"),
		FIND_ALL_TEAMS("SELECT * FROM teams"),
		FIND_TEAM_BY_ID("SELECT * FROM teams WHERE id = ?"),
		FIND_USER_BY_ID("SELECT * FROM users WHERE id = ?"),
		FIND_USER_TEAMS("SELECT * FROM users_teams WHERE user_id = ?"),
		FIND_TEAM_USERS("SELECT * FROM users_teams WHERE team_id = ?"),
		GET_USER("SELECT * FROM users WHERE login = ?"),
		GET_TEAM("SELECT * FROM teams WHERE name = ?"),
		DELETE_TEAM("DELETE FROM teams WHERE id = ?"),
		DELETE_USER("DELETE FROM users WHERE id = ?"),
		UPDATE_TEAM("UPDATE teams SET name = ? WHERE id = ?");
		private final String query;

		DBQueries(String query) {
			this.query = query;
		}

		public String getQuery() {
			return query;
		}
	}

	private DBManager() {}

	public static synchronized DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	public boolean insertUser(User user) throws DBException {
		String query = DBQueries.INSERT_USER.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, user.getLogin());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new DBException("No rows affected");
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getInt(1));
				} else {
					throw new DBException("No ID for new user");
				}
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't insert user into table", e);
		}
		return true;
	}

	public User getUser(String login) throws DBException {
		User user = User.createUser(login);
		String query = DBQueries.GET_USER.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setString(1, login);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				user.setId(resultSet.getInt(Fields.USER_ID));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get user from table", e);
		}
		return user;
	}

	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		String query = DBQueries.FIND_ALL_USERS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()){
				users.add(userToList(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get users list from table", e);
		}
		return users;
	}

	private User userToList(ResultSet resultSet) throws SQLException {
		User user = User.createUser(resultSet.getString(Fields.USER_LOGIN));
		user.setId(resultSet.getInt(Fields.USER_ID));
		return user;
	}

	public boolean deleteUsers(User... users) throws DBException {
		String query = DBQueries.DELETE_USER.getQuery();
		for (User user : users) {
			if (user == null) return false;
			try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
				 PreparedStatement ps = con.prepareStatement(query))
			{
				ps.setInt(1, user.getId());
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new DBException("Couldn't delete users from table", e);
			}
		}
		return true;
	}

	public boolean insertTeam(Team team) throws DBException {
		String query = DBQueries.INSERT_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, team.getName());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new DBException("No rows affected");
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				} else {
					throw new DBException("No ID for new team");
				}
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't insert team into table", e);
		}
		return true;
	}
	public Team getTeam(String name) throws DBException {
		Team team = Team.createTeam(name);
		String query = DBQueries.GET_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				team.setId(resultSet.getInt(Fields.TEAM_ID));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get team from table", e);
		}
		return team;
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		String query = DBQueries.FIND_ALL_TEAMS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()){
				teams.add(teamToList(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get teams list from table", e);
		}
		return teams;
	}

	private Team teamToList(ResultSet resultSet) throws SQLException {
		Team team = Team.createTeam(resultSet.getString(Fields.TEAM_NAME));
		team.setId(resultSet.getInt(Fields.TEAM_ID));
		return team;
	}

	public boolean deleteTeam(Team team) throws DBException {
		if (team == null) return false;
		String query = DBQueries.DELETE_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement stmt = con.prepareStatement(query))
		{
			stmt.setInt(1, team.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("Couldn't delete team from table", e);
		}
		return true;
	}

	public boolean setTeamsForUser(User user, Team... teams) throws DBException {
		if (user == null) {
			throw new DBException("No users to set", new NullPointerException());
		}
		String query = DBQueries.INSERT_TEAMS_FOR_USER.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL()))
		{
			con.setAutoCommit(false);
			try (PreparedStatement ps = con.prepareStatement(query)) {
				for (Team team : teams) {
					if (team == null) {
						con.rollback();
						throw new DBException("", new NullPointerException());
					}
					ps.setString(1, String.valueOf(user.getId()));
					ps.setString(2, String.valueOf(team.getId()));
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				con.rollback();
				throw new DBException("Couldn't set teams for users", e);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DBException("Couldn't set teams for users", e);
		}
		return true;
	}

	public List<Team> getUserTeams(User user) throws DBException {
		List<Team> teamList = new ArrayList<>();
		String query = DBQueries.FIND_USER_TEAMS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, user.getId());
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				teamList.add(userTeamsToList(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get user's teams", e);
		}
		return teamList;
	}

	private Team userTeamsToList(ResultSet rs) throws SQLException {
		String query = DBQueries.FIND_TEAM_BY_ID.getQuery();
		try (Connection connection = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement statement = connection.prepareStatement(query))
		{
			statement.setInt(1, rs.getInt("team_id"));
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			return teamToList(resultSet);
		}
	}

	public List<User> getTeamUsers(Team team) throws DBException {
		List<User> userList = new ArrayList<>();
		String query = DBQueries.FIND_TEAM_USERS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, team.getId());
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				userList.add(teamUsersToList(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Couldn't get team's users", e);
		}
		return userList;
	}

	private User teamUsersToList(ResultSet rs) throws SQLException {
		String query = DBQueries.FIND_USER_BY_ID.getQuery();
		try (Connection connection = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement statement = connection.prepareStatement(query))
		{
			statement.setInt(1, rs.getInt("user_id"));
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			return userToList(resultSet);
		}
	}


	public boolean updateTeam(Team team) throws DBException {
		String query = DBQueries.UPDATE_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setString(1, team.getName());
			ps.setInt(2, team.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException("Couldn't update the team", e);
		}
		return true;
	}

	private String getCONNECTION_URL() {
		String URL = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("app.properties"));
			URL = properties.getProperty("connection.url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return URL;
	}
}
