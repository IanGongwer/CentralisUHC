package com.iangongwer.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.main.Main;

public class ConnectionMYSQL {

	// Singleton of ConnectionMYSQL class
	private static ConnectionMYSQL single_instance;

	public static ConnectionMYSQL getInstance() {
		if (single_instance == null) {
			single_instance = new ConnectionMYSQL();
		}
		return single_instance;
	}

	FileConfiguration yml = Main.getInstance().getConfig();

	private Connection connection;

	private boolean connectedSuccessfully = false;

	public void connect() {
		try {
			Main.setRedisEnabled(false);
			this.connection = DriverManager.getConnection(
					"jdbc:mysql://" + yml.getString("Configuration.Host") + ":"
							+ yml.getInt("Configuration.Port") + "/"
							+ yml.getString("Configuration.Database") + "?useSSL=false",
					yml.getString("Configuration.Username"),
					yml.getString("Configuration.Password"));
			connectedSuccessfully = true;
			createSortedLeaderboardTable();
		} catch (SQLException e) {
			Main.setRedisEnabled(true);
			connectedSuccessfully = false;
		}
	}

	public void disconnect() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void createTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS player_statistics (player_name VARCHAR(48), player_uuid VARCHAR(128), player_kills int(11), player_deaths int(11), game_wins int(11), PRIMARY KEY (player_uuid)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createBannedTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS player_banned (player_name VARCHAR(48), player_uuid VARCHAR(128), PRIMARY KEY (player_uuid)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createGameInfoTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS game_information (players_left int(11), border_size int(11), game_time int(11)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createKillFeedTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS kill_feed (player_name VARCHAR(48), player_uuid VARCHAR(128), killer_name VARCHAR(48), killer_uuid VARCHAR(128), PRIMARY KEY (player_uuid)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTeamTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS player_teams (team_name VARCHAR(48), team_member VARCHAR(128)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeKillFeedData() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement(
					"TRUNCATE TABLE kill_feed");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createPlayer(UUID playerUUID) {
		try {
			if (!playerExists(playerUUID)) {
				PreparedStatement ps2 = getConnection().prepareStatement(
						"INSERT IGNORE INTO player_statistics (player_name, player_uuid) VALUES(?, ?)");
				ps2.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
				ps2.setString(2, playerUUID.toString());
				ps2.executeUpdate();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean playerExists(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT * FROM player_statistics WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public void addKill(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"UPDATE player_statistics SET player_kills = player_kills + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getTeamMembers(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"SELECT team_member FROM player_teams WHERE team_name = ?");
			ps.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
			ResultSet results = ps.executeQuery();
			String teamMembers;
			if (results.next()) {
				teamMembers = results.getString("team_member");
				return teamMembers;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void createTeam(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"INSERT IGNORE INTO player_teams (team_name, team_member) VALUES(?, ?)");
			ps.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
			ps.setString(2, Bukkit.getPlayer(playerUUID).getDisplayName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addTeam(UUID playerUUID, UUID leaderUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"UPDATE player_teams SET team_member = ? WHERE team_name = ?");
			ps.setString(1, getTeamMembers(leaderUUID) + "," + Bukkit.getPlayer(playerUUID).getDisplayName());
			ps.setString(2, Bukkit.getPlayer(leaderUUID).getDisplayName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disbandTeam(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"DELETE FROM player_teams WHERE team_name = ?");
			ps.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeTeam(UUID playerUUID, UUID leaderUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"UPDATE player_teams SET team_member = ? WHERE team_name = ?");
			ps.setString(1, getTeamMembers(leaderUUID).replace(Bukkit.getPlayer(playerUUID).getDisplayName() + ",", ""));
			ps.setString(2, Bukkit.getPlayer(leaderUUID).getDisplayName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getKills(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT player_kills FROM player_statistics WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			int kills;
			if (results.next()) {
				kills = results.getInt("player_kills");
				return kills;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void addDeath(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(
					"UPDATE player_statistics SET player_deaths = player_deaths + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getDeaths(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT player_deaths FROM player_statistics WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			int deaths;
			if (results.next()) {
				deaths = results.getInt("player_deaths");
				return deaths;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void addWin(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("UPDATE player_statistics SET game_wins = game_wins + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getWins(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT game_wins FROM player_statistics WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			int wins;
			if (results.next()) {
				wins = results.getInt("game_wins");
				return wins;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void emptyTable() {
		try {
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM player_statistics");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("DELETE FROM player_statistics WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UUID getMostKillsUUID() {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement(
							"SELECT player_name, player_kills FROM player_statistics ORDER BY player_kills DESC LIMIT 1");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if (Bukkit.getPlayer(playerName) != null) {
					return Bukkit.getPlayer(playerName).getUniqueId();
				}
				return Bukkit.getOfflinePlayer(playerName).getUniqueId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UUID getMostDeathsUUID() {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement(
							"SELECT player_name, player_deaths FROM player_statistics ORDER BY player_deaths DESC LIMIT 1");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if (Bukkit.getPlayer(playerName) != null) {
					return Bukkit.getPlayer(playerName).getUniqueId();
				}
				return Bukkit.getOfflinePlayer(playerName).getUniqueId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UUID getMostWinsUUID() {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement(
							"SELECT player_name, game_wins FROM player_statistics ORDER BY game_wins DESC LIMIT 1");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if (Bukkit.getPlayer(playerName) != null) {
					return Bukkit.getPlayer(playerName).getUniqueId();
				}
				return Bukkit.getOfflinePlayer(playerName).getUniqueId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addBannedPlayer(UUID playerUUID) {
		try {
			if (!bannedPlayerExists(playerUUID)) {
				PreparedStatement ps2 = getConnection().prepareStatement(
						"INSERT IGNORE INTO player_banned (player_name, player_uuid) VALUES(?, ?)");
				if (Bukkit.getPlayer(playerUUID) != null) {
					ps2.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
					ps2.setString(2, playerUUID.toString());
				} else {
					UUID playerOfflineUUID = Bukkit.getOfflinePlayer(playerUUID).getUniqueId();
					ps2.setString(1, Bukkit.getOfflinePlayer(playerOfflineUUID).getName());
					ps2.setString(2, playerOfflineUUID.toString());
				}
				ps2.executeUpdate();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeBannedPlayer(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM player_banned WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean bannedPlayerExists(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT * FROM player_banned WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public void createSortedLeaderboardTable() {
		PreparedStatement ps;
		try {
			ps = getConnection().prepareStatement("DROP TABLE sorted_leaderboard;");
			ps.executeUpdate();
			ps = getConnection().prepareStatement(
					"CREATE TABLE sorted_leaderboard (rank int(11) AUTO_INCREMENT, PRIMARY KEY (rank)) AS (SELECT * FROM player_statistics ORDER BY game_wins DESC, player_kills DESC, player_deaths ASC);");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerRank(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT rank FROM sorted_leaderboard WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ResultSet results = ps.executeQuery();
			int rank;
			if (results.next()) {
				rank = results.getInt("rank");
				return rank;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setPlayersLeft() {
		try {
			PreparedStatement ps2 = getConnection().prepareStatement(
					"UPDATE game_information SET players_left=?");
			ps2.setString(1, String.valueOf(GameManager.getInstance().getPlayers().size()));
			ps2.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setBorderSize(int borderSize) {
		try {
			PreparedStatement ps2 = getConnection().prepareStatement(
					"UPDATE game_information SET border_size=?");
			ps2.setString(1, String.valueOf(borderSize));
			ps2.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setGameTime(int gameTime) {
		try {
			PreparedStatement ps2 = getConnection().prepareStatement(
					"UPDATE game_information SET game_time=?");
			ps2.setString(1, String.valueOf(gameTime));
			ps2.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setGameState(GameState gameState) {
		try {
			PreparedStatement ps2 = getConnection().prepareStatement(
					"UPDATE game_information SET game_state=?");
			ps2.setString(1, String.valueOf(gameState.toString()));
			ps2.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addLiveKill(UUID playerUUID, UUID killerUUID) {
		try {
			PreparedStatement ps2 = getConnection().prepareStatement(
					"INSERT IGNORE INTO kill_feed (player_name, player_uuid, killer_name, killer_uuid) VALUES(?, ?, ?, ?)");
			ps2.setString(1, Bukkit.getPlayer(playerUUID).getDisplayName());
			ps2.setString(2, playerUUID.toString());
			if (Bukkit.getPlayer(killerUUID) != null) {
				ps2.setString(3, Bukkit.getPlayer(killerUUID).getDisplayName());
				ps2.setString(4, killerUUID.toString());
			} else {
				ps2.setString(3, null);
				ps2.setString(4, null);
			}
			ps2.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnectedSuccessfully() {
		return connectedSuccessfully;
	}

}