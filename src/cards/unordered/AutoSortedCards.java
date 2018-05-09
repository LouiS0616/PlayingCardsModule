package cards.unordered;

import card.Card;
import card.imitator.CardImitator;
import cards.Cards;
import cards.Observer;
import util.CollectionUtil;

import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

public class AutoSortedCards extends Cards {
    //
    // Generate methods
    public AutoSortedCards(String name) {
        this(name, Observer.STUB);
    }
    public AutoSortedCards(String name, Observer observer) {
        super(name, observer);
    }

    //
    // Check methods
    @Override
    public final int countCard() {
        return cardSet_.size();
    }

    //
    // Iterate methods
    @Override
    public final Iterator<Card> iterator() {
        return cardSet_.iterator();
    }
    @Override
    public final Stream<Card> stream() {
        return cardSet_.stream();
    }

    //
    // Methods related drawing
    @Override
    protected final Card pick() {
        return CollectionUtil.popElem(
            cardSet_, (Card)cardSet_.toArray()[randIndex()]
        );
    }
    @Override
    protected final Card pick(CardImitator purpose) {
        return CollectionUtil.popElem(
            cardSet_,
            stream()
                .filter(purpose::isEquivalent)
                .findFirst()
                .orElseThrow(CardNotFoundException::new)
        );
    }
    @Override
    public final void add(Card card) {
        if(!cardSet_.add(card)) {
            System.err.println("You may use deprecated cards.");
        }
    }

    //
    // Fields and utility
    private final SortedSet<Card> cardSet_ = new TreeSet<>();
    private final Random rand_ = new Random();

    private int randIndex() {
        return rand_.nextInt(cardSet_.size());
    }
}