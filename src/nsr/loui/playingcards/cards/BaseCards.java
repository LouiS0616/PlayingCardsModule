package nsr.loui.playingcards.cards;

import nsr.loui.playingcards.card.Card;
import nsr.loui.playingcards.card.Suit;
import nsr.loui.playingcards.card.imitator.CardImitator;
import nsr.loui.playingcards.card.imitator.IndividualCardImitator;
import nsr.loui.playingcards.card.imitator.SuitImitator;
import nsr.loui.playingcards.card.imitator.WildCardImitator;
import nsr.loui.playingcards.exceptions.CardNotEnoughException;
import nsr.loui.playingcards.exceptions.CardNotFoundException;
import nsr.loui.playingcards.exceptions.OwnerCertificateImproperException;
import nsr.loui.playingcards.util.PyLikePrinter;
import nsr.loui.playingcards.util.StreamAble;

import java.util.stream.Collectors;


/**
 * This class means cards and handles card transfer in safety way.
 */
public abstract class BaseCards implements Iterable<Card>, StreamAble<Card> {
    //
    // Generate methods
    BaseCards(String name) {
        this.name_ = name;
    }

    /**
     * Set owner when cards' owner has been uninitialized.
     * @param owner owner what should have THIS cards.
     * @throws OwnerCertificateImproperException when non-null THIS.owner is tried to overwrite.
     */
    final void setOwner(CardOwner owner) {
        if(this.certificate_ != null) {
            throw new OwnerCertificateImproperException("You can NOT reset card owner.");
        }

        this.certificate_ = owner.getCertificate();
    }


    /**
     * @param imitator purpose card imitator.
     * @return whether any card what is equivalent to imitator included in THIS or not.
     */
    public boolean include(CardImitator imitator) {
        return stream()
            .anyMatch(imitator::isEquivalent)
        ;
    }

    /**
     * @param imitator purpose card imitator.
     * @return how many cards what is equivalent to imitator included in THIS.
     */
    public int countCard(WildCardImitator imitator) {
        return (int)stream()
            .filter(imitator::isEquivalent)
            .count()
        ;
    }

    /**
     * @return #cards
     */
    public abstract int countCard();


    //
    // Methods related drawing

    /**
     * This method MUST be called only by BaseCards#pickFrom(BaseCards, IndividualCardImitator).
     * Because this method does not check card certificate at all.
     * @param card card you want to add THIS.
     */
    protected abstract void add(Card card);

    /**
     * This method behave randomly or not in accordance with its implementation.
     * @return a card imitator what mean drawable card.
     * @throws CardNotEnoughException when empty.
     */
    protected abstract IndividualCardImitator peek() throws CardNotEnoughException;

    /**
     * This method MUST be called only by BaseCards#pickFrom(BaseCards, IndividualCardImitator).
     * Do not call this method thoughtless avoid to lost card, because this method treats raw Card instance.
     * @param purpose card imitator what you want to draw.
     * @return drawn card.
     * @throws CardNotFoundException when card is not included in cards.
     */
    protected abstract Card draw(IndividualCardImitator purpose) throws CardNotFoundException;

    /**
     * This method is called when CARD added to THIS from FROM.
     * @param card added card.
     * @param from where card from.
     */
    protected abstract void update(Card card, BaseCards from);


    /**
     * In any way you draw card, you MUST use this method.
     * Because this method is responsible for check card certificate.
     * @param from where you want to pick a card from.
     * @param purpose what you want to draw.
     * @throws OwnerCertificateImproperException when certificates are difference between THIS and FROM.
     */
    public final void pickFrom(BaseCards from, IndividualCardImitator purpose) {
        if(this.certificate_ == null) {
            this.certificate_ = from.certificate_;
        }

        if(this.certificate_.isEquivalent(from.certificate_)) {
            Card card = from.draw(purpose);

            update(card, from);
            add(card);
        }
        else {
            throw new OwnerCertificateImproperException("You MUST NOT mix distinct deck.");
        }
    }


    //
    // Display methods

    /**
     * @return its own name what is set at Constructor.
     */
    @Override
    public String toString() {
        return this.name_;
    }

    private String toStringCards() {
        return stream()
            .map(Card::toString)
            .collect(Collectors.joining(", "))
        ;
    }
    private String toStringBreakdown() {
        return Suit.stream()
            .map(
                suit -> String.format("%s: %d", suit, countCard(new SuitImitator(suit)))
            )
            .collect(Collectors.joining(", "))
        ;
    }

    /**
     * Please use it for just for print debugging.
     */
    public void printInfo() {
        new PyLikePrinter("\n").print(
            "----------------------------------------------------------------",
            "Name:        " + name_,
            "Affiliation: " + certificate_,
            "#Card:       " + countCard(),
            "Breakdown:   " + toStringBreakdown(),
            "",
            toStringCards(),
            "----------------------------------------------------------------"
        );
    }


    //
    // Fields
    private final String name_;
    private CardOwnerCertificate certificate_;
}
