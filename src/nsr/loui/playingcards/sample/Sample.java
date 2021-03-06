package nsr.loui.playingcards.sample;

import nsr.loui.playingcards.card.RankedCard;
import nsr.loui.playingcards.card.Suit;
import nsr.loui.playingcards.card.comparator.JokersFirstComparator;
import nsr.loui.playingcards.card.comparator.JokersLastComparator;
import nsr.loui.playingcards.card.imitator.IndividualCardImitator;
import nsr.loui.playingcards.card.imitator.JokerImitator;
import nsr.loui.playingcards.card.imitator.RankImitator;
import nsr.loui.playingcards.card.imitator.SuitImitator;
import nsr.loui.playingcards.card.imitator.UnitedImitator;
import nsr.loui.playingcards.cards.Trash;
import nsr.loui.playingcards.cards.Deck;
import nsr.loui.playingcards.cards.Hand;
import nsr.loui.playingcards.observer.PrintObserver;

import java.util.Comparator;

/**
 * Sample how to use this module.
 */
class Sample {
    public static void main(String[] args) {
        //
        // Make a deck.
        Deck deck = new Deck();

        //
        // Remove unnecessary cards.
        UnitedImitator unitedImitator = new UnitedImitator(
            new RankImitator(11),
            new RankImitator(12),
            new RankImitator(13),
            new SuitImitator(Suit.CLUB)
        );

        Trash trash = Trash.makeTrash();
        trash.divideFrom(deck, unitedImitator);

        //
        // Shuffle deck and make a hand with observer.
        deck.shuffle();
        Hand hand = new Hand("My Hand", new PrintObserver());
        hand.pickFrom(deck, 10);

        //
        // Draw joker from deck.
        IndividualCardImitator jokerImitator = new JokerImitator();
        if(deck.include(jokerImitator)) {
            hand.pickFrom(deck, jokerImitator);
        }
        else {
            System.out.println("You already have two jokers.");
        }

        //
        // Print info.
        hand.printInfo();

        //
        // Sorting.
        deck.sort(
            new JokersLastComparator() {
                @Override
                protected int compare(RankedCard rankedCard1, RankedCard rankedCard2) {
                    return Comparator
                        .comparing(RankedCard::getSuit)
                        .thenComparingInt(RankedCard::getRank)
                        .compare(rankedCard1, rankedCard2)
                    ;
                }
            }
        );
        deck.printInfo();
    }
}
