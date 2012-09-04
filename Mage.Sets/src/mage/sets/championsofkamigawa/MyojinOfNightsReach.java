/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.championsofkamigawa;

import java.util.UUID;

import mage.Constants;
import mage.Constants.CardType;
import mage.Constants.Rarity;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.common.CastFromHandCondition;
import mage.abilities.condition.common.HasCounterCondition;
import mage.abilities.costs.common.RemoveCountersSourceCost;
import mage.abilities.decorator.ConditionalContinousEffect;
import mage.abilities.decorator.ConditionalOneShotEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.continious.GainAbilitySourceEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.keyword.IndestructibleAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.counters.CounterType;
import mage.game.Game;
import mage.players.Player;
import mage.watchers.common.CastFromHandWatcher;

/**
 * @author LevelX
 */
public class MyojinOfNightsReach extends CardImpl<MyojinOfNightsReach> {

    public MyojinOfNightsReach(UUID ownerId) {
        super(ownerId, 126, "Myojin of Nights Reach", Rarity.RARE, new CardType[]{CardType.CREATURE}, "{5}{B}{B}{B}");
        this.expansionSetCode = "CHK";
        this.supertype.add("Legendary");
        this.subtype.add("Spirit");

        this.color.setBlack(true);
        this.power = new MageInt(5);
        this.toughness = new MageInt(2);

        this.addWatcher(new CastFromHandWatcher());

        // Myojin of Night's Reach enters the battlefield with a divinity counter on it if you cast it from your hand.
        this.addAbility(new EntersBattlefieldAbility(new ConditionalOneShotEffect(new AddCountersSourceEffect(CounterType.DIVINITY.createInstance()), new CastFromHandCondition(), ""), "{this} enters the battlefield with a divinity counter on it if you cast it from your hand"));
        // Myojin of Night's Reach is indestructible as long as it has a divinity counter on it.
        this.addAbility(new SimpleStaticAbility(Constants.Zone.BATTLEFIELD, new ConditionalContinousEffect(new GainAbilitySourceEffect(new IndestructibleAbility(), Constants.Duration.WhileOnBattlefield),
                new HasCounterCondition(CounterType.DIVINITY), "{this} is indestructible as long as it has a divinity counter on it")));
        // Remove a divinity counter from Myojin of Night's Reach: Each opponent discards his or her hand.
        Ability ability = new SimpleActivatedAbility(Constants.Zone.BATTLEFIELD, new MyojinOfNightsReachEffect(), new RemoveCountersSourceCost(CounterType.DIVINITY.createInstance()));
        this.addAbility(ability);
    }

    public MyojinOfNightsReach(final MyojinOfNightsReach card) {
        super(card);
    }

    @Override
    public MyojinOfNightsReach copy() {
        return new MyojinOfNightsReach(this);
    }
}

class MyojinOfNightsReachEffect extends OneShotEffect<MyojinOfNightsReachEffect> {
    public MyojinOfNightsReachEffect() {
        super(Constants.Outcome.Discard);
        staticText = "Each opponent discards his or her hand";
    }

    public MyojinOfNightsReachEffect(final MyojinOfNightsReachEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        for (UUID opponentId : game.getOpponents(source.getControllerId())) {
                Player opponent = game.getPlayer(opponentId);
                for (Card c : opponent.getHand().getCards(game)) {
                    opponent.discard(c, source, game);
                }
        }
        return true;
    }

    @Override
    public MyojinOfNightsReachEffect copy() {
        return new MyojinOfNightsReachEffect(this);
    }

}