package Ghreborn.model.npcs.boss.zulrah.impl;

import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.npcs.boss.zulrah.ZulrahLocation;
import Ghreborn.model.npcs.boss.zulrah.ZulrahStage;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.CombatType;

public class MeleeStageSix extends ZulrahStage {

	public MeleeStageSix(Zulrah zulrah, Client player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead
				|| player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		if (zulrah.getNpc().totalAttacks > 1 && zulrah.getNpc().attackTimer == 9) {
			player.getZulrahEvent().changeStage(7, CombatType.RANGE, ZulrahLocation.EAST);
			zulrah.getNpc().totalAttacks = 0;
			zulrah.getNpc().setFacePlayer(true);
			container.stop();
			return;
		}
	}

}
