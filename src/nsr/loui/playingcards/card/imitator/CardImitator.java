package nsr.loui.playingcards.card.imitator;

import nsr.loui.playingcards.card.Card;

/**
 * Classes implements this interface imitate a card.
 * Use imitator proactively instead of concrete Card instance directory.
 */
public interface CardImitator {
    /**
     * @param card card judged equivalent to THIS.
     * @return whether THIS is equivalent to CARD or not.
     */
    boolean isEquivalent(Card card);
}
