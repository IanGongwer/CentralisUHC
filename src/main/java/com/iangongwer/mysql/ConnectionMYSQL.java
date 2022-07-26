package com.iangongwer.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

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
					"CREATE TABLE IF NOT EXISTS player_information (player_name VARCHAR(48), player_uuid VARCHAR(48), player_kills int(11), player_deaths int(11), game_wins int(11), PRIMARY KEY (player_uuid)");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createPlayer(UUID playerUUID) {
		try {
			if (!playerExists(playerUUID)) {
				PreparedStatement ps2 = getConnection().prepareStatement(
						"INSERT IGNORE INTO player_information (player_name, player_uuid) VALUES(?, ?)");
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
					.prepareStatement("SELECT * FROM player_information WHERE player_uuid = ?");
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
					"UPDATE player_information SET player_kills = player_kills + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getKills(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT player_kills FROM player_information WHERE player_uuid = ?");
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
					"UPDATE player_information SET player_deaths = player_deaths + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getDeaths(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT player_deaths FROM player_information WHERE player_uuid = ?");
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
					.prepareStatement("UPDATE player_information SET game_wins = game_wins + 1 WHERE player_uuid = ?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getWins(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT game_wins FROM player_information WHERE player_uuid = ?");
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
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM player_information");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(UUID playerUUID) {
		try {
			PreparedStatement ps = getConnection().prepareStatement("DELETE FROM player_information WHERE UUID?");
			ps.setString(1, playerUUID.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UUID getMostKillsUUID() {
		try {
			PreparedStatement ps = getConnection()
					.prepareStatement("SELECT player_name, MAX(player_kills) FROM player_information");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if(Bukkit.getPlayer(playerName) != null) {
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
					.prepareStatement("SELECT player_name, MAX(player_deaths) FROM player_information");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if(Bukkit.getPlayer(playerName) != null) {
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
					.prepareStatement("SELECT player_name, MAX(game_wins) FROM player_information");
			ResultSet results = ps.executeQuery();
			String playerName;
			if (results.next()) {
				playerName = results.getString("player_name");
				if(Bukkit.getPlayer(playerName) != null) {
					return Bukkit.getPlayer(playerName).getUniqueId();
				}
				return Bukkit.getOfflinePlayer(playerName).getUniqueId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isConnectedSuccessfully() {
		return connectedSuccessfully;
	}

}