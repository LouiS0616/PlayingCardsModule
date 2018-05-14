package cards;

import card.Card;
import observer.Observer;

/**
 * When you construct a deck, a set of cards are set automatically.
 * These cards' owner is deck instance.
 */
public final class Deck extends OrderedCards implements CardOwner {
    private static int serialId_ = 0;

    //
    // Generate methods
    public Deck() {
        this(Observer.STUB);
    }
    public Deck(Observer observer) {
        super("Deck" + serialId_, observer);

        this.affiliation_ = new CardOwnerCertificate("Deck" + serialId_);
        setCards(
            Card.makeCards$for_deck(this.affiliation_), this
        );

        ++serialId_;
    }

    //
    // Affiliation
    @Override
    public CardOwnerCertificate getAffiliation() {
        return this.affiliation_;
    }
    private final CardOwnerCertificate affiliation_;
}