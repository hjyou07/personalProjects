#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "a4.h"
#include <time.h>

#define PRINT_DEBUG 1

//----------------------------------------
// Deck functions
//----------------------------------------
// Implement these functions in deck.c.
// Creates the deck to be used for NEUchre.
// Assume that the value of cards are:
// Nine=9; Ten=10; Jack=11; and so on, up to Ace=14.

// Creates the deck, initializing any fields necessary.
// Returns a pointer to the deck, which has been allocated on the heap.
Deck* CreateDeck() {
  Deck *deckPointer = (Deck *)malloc(sizeof(Deck));
  deckPointer->top_card = -1;
  return deckPointer;
}

// Adds a card to the top of the deck.
// Returns a pointer to the deck.
Deck* PushCardToDeck(Card* card, Deck* deck) {
  while (deck->top_card != (kNumCardsInDeck - 1)) {
    // update the (int) top_card so that it gives(will give)
    // an index to the newly added card
    deck->top_card += 1;
    // deck->cards = array of individual Card pointers
    // put the "Card *" in the "cards" array at cards[top card's index]
    deck->cards[deck->top_card] = card;
    return deck;
  }
  printf("the deck is full, can't add card\n");
  return deck;
}

// Shows the top card, but does not remove it from the stack.
// Returns a pointer to the top card.
// If the deck is empty, return NULL.
Card* PeekAtTopCard(Deck* deck) {
  if (IsDeckEmpty(deck)) {
    return NULL;
  } else {
    return deck->cards[deck->top_card];
  }
}

// Removes the top card from the deck and returns it.
// The card should NOT be freed at this point; it is the
// responsibility of the caller to free the card at the
// appropriate time.
// Returns a pointer to card removed.
// If the deck is empty, return NULL.
Card* PopCardFromDeck(Deck* deck) {
  Card *toBeRemoved;
  if (IsDeckEmpty(deck)) {
    return NULL;
  } else {
    // save the "current" top card (which is to be removed)
    // into a variable and set it aside, this is what we will return.
    // it also lets us update the Deck without losing the card's information
    toBeRemoved = deck->cards[deck->top_card];
    // update the Deck,
    // now the slot in the deck that held the top_card doesn't hold anything
    deck->cards[deck->top_card] = NULL;
    // update the top_card's index
    deck->top_card -= 1;
    // I want to know if I removed the last item from the deck,
    // in this case the deck->top_card I just updated will be -1
    }
    return toBeRemoved;
}

// Determines if the deck is empty.
// Returns 1 if the Deck is empty, 0 otherwise.
int IsDeckEmpty(Deck* deck) {
  return deck->top_card == -1;
}

// Destorys the empty deck, this is a helper function for DestroyDeck.
// the deck variable which has been allocated on the heap can freed
void DestroyEmptyDeck(Deck* deck) {
  assert(IsDeckEmpty(deck));
  free(deck);
}

// Destroys the deck, freeing any memory allocated
// for this deck (the cards and the deck).
// DestroyDeck should call DestroyCard on all of the
// cards in the deck.
void DestroyDeck(Deck* deck) {
  while (!IsDeckEmpty(deck)) {
    Card *tobeRemoved = PopCardFromDeck(deck);
    DestroyCard(tobeRemoved);
  }
  DestroyEmptyDeck(deck);
}

//----------------------------------------
// Game functions
//----------------------------------------

// Swaps two elements, helper function for Shuffle
void Swap(Card *a, Card *b) {
  Card temp = *a;
  *a = *b;
  *b = temp;
}

// Shuffle the deck.
// Put them in a random order.
void Shuffle(Deck *deck) {
  int i;
  int j;
  // use a different seed value so that we don't get same
  // result each time we run this program
  srand(time(NULL));
  // start from the first element and iterate through all items
  for (i = 0; i < kNumCardsInDeck; i++) {
    // pick a random index between 0 and 23 (remainder of /24)
    j = rand() % kNumCardsInDeck;
    // swap cards[i] with the element at random index j
    Swap(deck->cards[i], deck->cards[j]);
  }
}

// Create a Deck for this game, and add any
// needed cards to the deck.
// Return a pointer to the deck to be used for the game
// needed cards include combinations of below:
// enum suit {HEARTS, CLUBS, SPADES, DIAMONDS}
// enum name {NINE = 9, TEN, JACK, QUEEN, KING, ACE};
Deck* PopulateDeck() {
  // CreateDeck() creates an empty deck on a heap, returns a pointer to it
  Deck *deckPointer = CreateDeck();
  Suit s;
  Name n;
  for (s = HEARTS; s <= DIAMONDS; s++) {
    for (n = NINE; n <= ACE; n++) {
      Card *cardPointer = CreateCard(s, n);
      PushCardToDeck(cardPointer, deckPointer);
    }
  }
  return deckPointer;
}

// Checks if each player has kNumCardsInHand
// this is a helper function for Deal.
// Returns 1 if the hands are full, 0 otherwise.
int HandsNotFull(Hand *aHand) {
  return aHand->num_cards_in_hand < kNumCardsInHand;
}

// Distribute the card to a specified player,
// this is a helper function for Deal.
// hand->first_card is a CardNode
// hand->first_card->this_card is a Card
// hand->num_cards_in_hand is an int
void Distribute(Deck *aDeck, Hand *aHand) {
  Card *card = PopCardFromDeck(aDeck);
  AddCardToHand(card, aHand);
}

// Given a deck (assume that it is already shuffled),
// take the top card from the deck and alternately give
// it to player 1 and player 2, until they both have
// kNumCardsInHand.
void Deal(Deck *aDeck, Hand *p1hand, Hand *p2hand) {
  int i;
  while (HandsNotFull(p1hand) || HandsNotFull(p2hand)) {
      Distribute(aDeck, p1hand);
      Distribute(aDeck, p2hand);
  }
}
