package net.demilich.metastone.game.spells;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import net.demilich.metastone.game.GameContext;
import net.demilich.metastone.game.GameTag;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.CardCollection;
import net.demilich.metastone.game.entities.Actor;
import net.demilich.metastone.game.entities.Entity;
import net.demilich.metastone.game.entities.EntityType;
import net.demilich.metastone.game.entities.minions.Minion;
import net.demilich.metastone.game.entities.minions.Race;
import net.demilich.metastone.game.spells.desc.SpellArg;
import net.demilich.metastone.game.spells.desc.SpellDesc;
import net.demilich.metastone.game.spells.desc.filter.EntityFilter;
import net.demilich.metastone.game.spells.desc.filter.Operation;
import net.demilich.metastone.game.targeting.EntityReference;

public class SpellUtils {

	public static Card drawFromDeck(GameContext context, Player player) {
		return context.getLogic().drawCard(player.getId());
	}

	public static CardCollection getCards(CardCollection source, Predicate<Card> filter) {
		CardCollection result = new CardCollection();
		for (Card card : source) {
			if (filter.test(card)) {
				result.add(card);
			}
		}
		return result;
	}

	public static Card getRandomCard(CardCollection source, Predicate<Card> filter) {
		CardCollection result = getCards(source, filter);
		if (result.isEmpty()) {
			return null;
		}
		return result.getRandom();
	}

	public static <T> T getRandomTarget(List<T> targets) {
		int randomIndex = ThreadLocalRandom.current().nextInt(targets.size());
		return targets.get(randomIndex);
	}

	public static List<Actor> getValidRandomTargets(List<Entity> targets) {
		List<Actor> validTargets = new ArrayList<Actor>();
		for (Entity entity : targets) {
			Actor actor = (Actor) entity;
			if (!actor.isDead() || actor.getEntityType() == EntityType.HERO) {
				validTargets.add(actor);
			}

		}
		return validTargets;
	}

	public static List<Entity> getValidTargets(List<Entity> allTargets, EntityFilter filter) {
		if (filter == null) {
			return allTargets;
		}
		List<Entity> validTargets = new ArrayList<>();
		for (Entity entity : allTargets) {
			if (filter.matches(entity)) {
				validTargets.add(entity);
			}
		}
		return validTargets;
	}

	public static int hasHowManyOfRace(Player player, Race race) {
		int count = 0;
		for (Minion minion : player.getMinions()) {
			if (minion.getRace() == race) {
				count++;
			}
		}
		return count;
	}

	public static boolean hasMinionOfRace(Player player, Race race) {
		for (Minion minion : player.getMinions()) {
			if (minion.getRace() == race) {
				return true;
			}
		}
		return false;
	}

	public static boolean holdsMinionOfRace(Player player, Race race) {
		for (Card card : player.getHand()) {
			if (card.getTag(GameTag.RACE) == race) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean evaluateOperation(Operation operation, int actualValue, int targetValue) {
		switch (operation) {
		case EQUAL:
			return actualValue == targetValue;
		case GREATER:
			return actualValue > targetValue;
		case HAS:
			return actualValue > 0;
		case LESS:
			return actualValue < targetValue;
		}
		return false;
	}
	
	public static void castChildSpell(GameContext context, Player player, SpellDesc spell, Entity source, Entity target) {
		EntityReference sourceReference = source != null ? source.getReference() : null;
		EntityReference targetReference = spell.getTarget();
		if (targetReference == null && target != null) {
			targetReference = target.getReference();
		}
		context.getLogic().castSpell(player.getId(), spell, sourceReference, targetReference);
	}
	
	public static Card[] getCards(SpellDesc spell) {
		String[] cardNames = null;
		if (spell.contains(SpellArg.CARDS)) {
			cardNames = (String[]) spell.get(SpellArg.CARDS);
		} else {
			cardNames = new String[1];
			cardNames[0] = (String) spell.get(SpellArg.CARD);
		}
		Card[] cards = new Card[cardNames.length];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = CardCatalogue.getCardById(cardNames[i]);
		}
		return cards;
	}

	private SpellUtils() {
	}

}
