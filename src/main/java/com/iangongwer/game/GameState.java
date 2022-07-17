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
}