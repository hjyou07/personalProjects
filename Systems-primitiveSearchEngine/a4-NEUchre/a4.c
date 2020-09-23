#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <assert.h>

#include "a4.h"

#define kPrintDebug 1

// Implement the Hand and other functions in here

//----------------------------------------
// Card functions
//----------------------------------------

// Creates a card, initializing the suit and name to that specified.
// Returns a pointer to the new card, which has beel allocated on the heap.
// It is the responsibility of the caller to call destroyCard()
// when it is done with the Card.
Card* CreateCard(Suit suit, Name name) {
  Card *cardPointer = (Card *)malloc(sizeof(Card));
  cardPointer->name = name;
  cardPointer->suit = suit;
  return cardPointer;
}

// Destroys the card, freeing any memory allocated for the card.
void DestroyCard(Card* card) {
  free(card);
}

//----------------------------------------
// Hand functions
//----------------------------------------

// Creates a Hand struct and initializes any necessary fields.
// Returns a pointer to the new Hand, which has been allocated on the heap.
Hand* CreateHand() {
  Hand *handPointer = (Hand *)malloc(sizeof(Hand));
  handPointer->num_cards_in_hand = 0;
  handPointer->first_card = NULL;
  return handPointer;
}

// Creates a CardNode struct and initializes with a passed Card struct
// this is a helper function for all the Hand functions,
// as they all just take in Card pointer as a parameter when Hand has
// int num_cards_in_hand and CardNode *first_card as its members
CardNode* CreateCardNode(Card *card) {
  CardNode *cardNodePointer = (CardNode *)malloc(sizeof(CardNode));
  cardNodePointer->this_card = card;
  return cardNodePointer;
}

// Adds a card to the hand.
void AddCardToHand(Card *card, Hand *hand) {
  // create a cardNode with a given card
  CardNode *cardNode = CreateCardNode(card);
  // make the next_card of added cardNode point to the old first_card in hand
  cardNode->next_card = hand->first_card;
  // set the prev_card of added cardNode as NULL
  cardNode->prev_card = NULL;
  // set the prev_card of the current head as added cardNode
  // if im adding the first element, don't do this
  if (!IsHandEmpty(hand)) {
    hand->first_card->prev_card = cardNode;
  }
  // set the head point to the added cardNode
  hand->first_card = cardNode;
  // increment the num_cards_in_hand
  hand->num_cards_in_hand += 1;
}

// Finds the hode of the hand a card passed as a parameter is a member of.
// this is a helper function for RemoveCardFromHand(),
// as it returns the found card node when called.
CardNode* FindCardLocation(Card *card, Hand *hand) {
  CardNode *curr_card = hand->first_card;
  while (card != curr_card->this_card) {
  curr_card = curr_card->next_card;
  }
  return curr_card;
}

// change the NEXT CARD'S PREVIOUS CARD to point at current card's previous card
// only if the card is NOT the last card (includes the head case too)
void ChangeNextCardNode(CardNode *curr_card) {
  curr_card->next_card->prev_card = curr_card->prev_card;
}

// change the PREVIOUS CARD'S NEXT CARD to point at current card's next card
// only if the card is NOT the first card (includes removing the last item)
void ChangePrevCardNode(CardNode *curr_card) {
  curr_card->prev_card->next_card = curr_card->next_card;
}

// Removes a card from the hand.
// Does not free the card; it is the responsibility
// of the caller to free the card at the appropriate
// time.
// Returns a pointer to the card that is no longer in the hand.
Card* RemoveCardFromHand(Card *card, Hand *hand) {
  CardNode *curr_card = FindCardLocation(card, hand);
  // what follows are not if/elif because they're not entirely exclusive
  if (curr_card->prev_card == NULL) {
    // set the head to be the second card(first_card->next_card)
    // this should also work if the first card is the only card
    hand->first_card = hand->first_card->next_card;
  }
  // if the card to be removed is NOT the last card
  if (curr_card->next_card != NULL) {
    ChangeNextCardNode(curr_card);
  }
  // if the card to be removed is NOT the first card
  if (curr_card->prev_card != NULL) {
    ChangePrevCardNode(curr_card);
  }
  hand->num_cards_in_hand -= 1;
  free(curr_card);
  return card;
}

// Determines if there are any cards in the hand.
// Return 1 if the hand is empty; 0 otherwise.
int IsHandEmpty(Hand* hand) {
  return (hand->num_cards_in_hand == 0);
}

// Destroys the empty hand, helper function for DestroyHand
void DestroyEmptyHand(Hand* hand) {
  assert(IsHandEmpty(hand));
  free(hand);
}

// Destroys the hand, freeing any memory allocated for it.
void DestroyHand(Hand* hand) {
  while (!IsHandEmpty(hand)) {
    Card *card = hand->first_card->this_card;
    RemoveCardFromHand(card, hand);
  }
  DestroyEmptyHand(hand);
}

// do I need this? call it whenever i remove a thing
void DestroyCardNode(CardNode *cardNode) {
  free(cardNode);
}

// Returns 1 if the hand has a card with the same suit as the lead card
int HandHasSameSuit(Hand *hand, Card *lead_card) {
  // ********MAKE SURE YOUR TEMP VARIABLE IS CARDNODE NOT HAND**********//
  CardNode *tempNode = hand->first_card;
  while (tempNode != NULL) {
    if (lead_card->suit == tempNode->this_card->suit) {
      return 1;
    }
    tempNode = tempNode->next_card;
  }
  // exited the while loop, you never found the same suit
  return 0;
}

// Given a lead card, a players hand, and the card the player wants
// to play, is it legal?
// If the player has a card of the same suit as the ledCard, they
// must play a card of the same suit.
// If the player does not have a card of the same suit, they can
// play any card.
// Returns 1 if the move is legal; 0 otherwise.
int IsLegalMove(Hand *hand, Card *lead_card, Card *played_card) {
  int handHasSuit = HandHasSameSuit(hand, lead_card);
  if (handHasSuit && (played_card->suit == lead_card->suit)) {
    return 1;
  } else if (handHasSuit && (played_card->suit != lead_card->suit)) {
    return 0;
  }
  // all the other cases: if hand doesn't have suit, any move is legal
  return 1;
}

// Given two cards that are played in a hand, which one wins?
// If the suits are the same, the higher card name wins.
// If the suits are not the same, player 1 wins, unless player 2 played trump.
// Returns 1 if the person who led won, 0 if the person who followed won.
int WhoWon(Card *lead_card, Card *followed_card, Suit trump) {
  if (lead_card->suit == followed_card->suit) {
    return lead_card->name > followed_card->name;
  } else {
    if (followed_card->suit == trump) {
      return 0;
    }
  return 1;
  }
}

// Take all the cards out of a given hand, and put them
// back into the deck.
// uh where is this being tested?
void ReturnHandToDeck(Hand *hand, Deck *deck) {
  while (hand->first_card != NULL) {
  Card *card = RemoveCardFromHand(hand->first_card->this_card, hand);
  PushCardToDeck(card, deck);
  hand->first_card == hand->first_card->next_card;
  }
}
