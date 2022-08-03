package com.iangongwer.game;

public enum GameState {
	Lobby, Scattering, InGame, End;

	private static GameState state;

	public static void setState(GameState state) {
		GameState.state = state;
	}

	public static boolean isState(GameState state) {
		return GameState.state == state;
	}

	public static GameState getState() {
		return state;
	}

	public static boolean isLobby() {
		return isState(Lobby);
	}

	public static boolean isScattering() {
		return isState(Scattering);
	}

	public static boolean isInGame() {
		return isState(InGame);
	}

	public static boolean isEnd() {
		return isState(End);
	}
}