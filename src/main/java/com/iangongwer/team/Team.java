package com.iangongwer.team;

import java.util.ArrayList;
import java.util.UUID;

public class Team {

	UUID leader;
	ArrayList<UUID> members = new ArrayList<UUID>();
	ArrayList<UUID> deceased = new ArrayList<UUID>();
	int size;
	ArrayList<UUID> invited = new ArrayList<UUID>();

	Team(UUID leader) {
		this.leader = leader;
		this.members.add(leader);
		this.size = 1;
	}

}