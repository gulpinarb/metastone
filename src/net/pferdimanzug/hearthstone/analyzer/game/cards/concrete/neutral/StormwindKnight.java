package net.pferdimanzug.hearthstone.analyzer.game.cards.concrete.neutral;

import net.pferdimanzug.hearthstone.analyzer.game.GameTag;
import net.pferdimanzug.hearthstone.analyzer.game.cards.MinionCard;
import net.pferdimanzug.hearthstone.analyzer.game.cards.Rarity;
import net.pferdimanzug.hearthstone.analyzer.game.entities.heroes.HeroClass;
import net.pferdimanzug.hearthstone.analyzer.game.entities.minions.Minion;

public class StormwindKnight extends MinionCard {

	public StormwindKnight() {
		super("Stormwind Knight", Rarity.FREE, HeroClass.ANY, 4);
	}

	@Override
	public Minion summon() {
		return createMinion(2, 5, GameTag.CHARGE);
	}

}
