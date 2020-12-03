package battleships.server.db;

import battleships.server.exception.DbException;
import battleships.server.exception.RegistrationException;
import battleships.server.game.Player;
import battleships.util.RegistrationError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {
	private final static int UNIQUE_CONSTRAINT_ERROR_CODE = 19;
	private static UserDatabase instance;
	private DBConnection dbConnection = DBConnection.getInstance();

	public static UserDatabase getInstance() {
		if (instance == null) {
			instance = new UserDatabase();
		}
		return instance;
	}

	public Player loadPlayerByName(String username) {
		try (Connection con = dbConnection.getConnection()) {
			try {
				Player player = loadPlayerByName(username, con);
				con.commit();
				return player;
			} finally {
				con.rollback();
			}
		} catch (SQLException throwables) {
			throw new DbException("Error loading user from database", throwables);
		}
	}

	private Player loadPlayerByName(String username, Connection connection) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("SELECT id, username, password FROM players WHERE username = ?");
		pstmt.setString(1, username);
		pstmt.execute();
		ResultSet rs = pstmt.getResultSet();
		while (rs.next()) {
			return parsePlayer(rs);
		}
		return null;
	}

	/**
	 * Saves a player to the database
	 * @param player A player with a already hashed password
	 * @return the Player with his Id
	 */
	public Player savePlayer(Player player) {
		try (Connection con = dbConnection.getConnection()) {
			try {
				Player savedPlayer = savePlayer(player, con);
				con.commit();
				return savedPlayer;
			} finally {
				con.rollback();
			}
		} catch (SQLException throwables) {
			if(throwables.getErrorCode() == UNIQUE_CONSTRAINT_ERROR_CODE) {
				throw new RegistrationException("Username already taken!", RegistrationError.USER_EXISTS);
			}
			throw new DbException("Error saving user to database", throwables);
		}
	}

	private Player savePlayer(Player player, Connection connection) throws SQLException {
		Connection con = dbConnection.getConnection();
		con.setAutoCommit(false);
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO players (username, password) VALUES (?, ?)");
		pstmt.setString(1, player.getUsername());
		pstmt.setString(2, player.getPassword());
		pstmt.execute();
		return loadPlayerByName(player.getUsername(), connection);
	}

	private Player parsePlayer(ResultSet rs) throws SQLException {
		return new Player(rs.getInt(1), rs.getString(2), rs.getString(3));
	}

}
