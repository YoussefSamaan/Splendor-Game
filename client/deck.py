from random import shuffle
from time import sleep
from typing import Dict
from board import Board
from singleton import Singleton
from color import Color
from bonus import Bonus
from card import Card
from cost import Cost
import pygame


class Deck:
    x_DistanceBetweenCardsToBoardWidthRatio = 1/30 # The ratio of the distance between cards on the x-axis to the width of the board
    y_DistanceBetweenCardsToBoardHeightRatio = 1/22 # The ratio of the distance between cards on the y-axis to the height of the board
    x_MarginToBoardWidthRatio = 1/60 # The margin between the left side of the board and the left side of the deck cover
    y_MarginToBoardHeightRatio = 0.44 # First deck starts at 0.44 * board height y-coordinate

    def __init__(self, level: int, color: Color):
        """
        Initializes the deck
        """
        self.cardSlots = 4
        self.level = level
        self.color = color
        self.image = self.getImage()
        self.rect = self.image.get_rect()
        self.cards = []
        self.cardsOnDisplay: Dict[int, Card] = {}
        self.board = Board.instance()
        self.initDeck()
        self.shuffle()
        self.fillEmptySlots()


    def shuffle(self):
        """
        Shuffles the deck
        """
        shuffle(self.cards)

    def fillEmptySlots(self):
        """
        Fills the empty slots in the deck with cards
        """
        while len(self.cardsOnDisplay) < self.cardSlots:
            self.drawCard()

    def drawCard(self) -> Card:
        """
        Draws a card from the deck

        :pre: The deck is not empty
        :return: The card that was drawn
        """
        card = self.cards.pop()
        self._addCardToDisplay(card)
        return card

    def _addCardToDisplay(self, card: Card):
        """
        Adds a card to the cards on display. The card is added to the first empty slot

        :param: card: The card to add to the display
        :pre: There is an empty slot in the deck
        """
        for i in range(1, self.cardSlots+1):
            if i not in self.cardsOnDisplay.keys():
                self.cardsOnDisplay[i] = card
                return

    def display(self, screen):
        """
        Displays the cards on the board, and the deck cover
        """
        self.displayDeckCover(screen)
        for slotPos, card in self.cardsOnDisplay.items():
            self.drawCardToBoard(screen, card, slotPos)

    def displayDeckCover(self, screen):
        """
        Displays the deck cover on the board surface
        """
        width, height = Deck.getDeckDisplaySize(self.board)
        x = self.getDeckX()
        if self.color == Color.RED:
            x -= width
        y = self.getDeckY()
        image = pygame.transform.scale(self.image, (int(width), int(height)))
        screen.blit(image, (x, y))

    def drawCardToBoard(self, screen, card, slotPos):
        """
        Draws a card on the board surface

        :param: screen: The screen to draw the card on
        :param: card: The card to draw
        :param: slotPos: The position of the card slot to draw the card to
        """
        card.draw(screen, *self.getCardCoords(slotPos))

    def getCardCoords(self, slotPos):
        """
        Returns the position of the next card to be drawn
        """
        if self.color == Color.RED:
            x = self.getDeckX() - slotPos * self.xDistanceBetweenCardStartToNextStart() - Card.getCardSize(self.board)[0]
        else:
            x = self.getDeckX() + slotPos * self.xDistanceBetweenCardStartToNextStart()
        y = self.getDeckY()
        return (x, y)

    @staticmethod
    def getDeckDisplaySize(board):
        """
        Returns the size of the deck cover based on the size of the board
        """
        return Card.getCardSize(board)

    def getDeckPos(self):
        """
        Returns the position of the deck cover based on the size of the board
        """
        return (self.getDeckX(), self.getDeckY())
    
    def getDeckX(self):
        """
        Returns the x-coordinate of the side of the deck nearest to the board based on the size of the board
        """
        board = self.board
        if self.color == Color.RED:
            return board.getX() + board.getWidth()*(1 - self.x_MarginToBoardWidthRatio) # Red deck is on the right side of the board

        return board.getX() + board.getWidth() * self.x_MarginToBoardWidthRatio

    def getDeckY(self):
        """
        Returns the y-coordinate of the deck cover based on the size of the board
        """
        board = self.board
        distanceFromTop = (3 - self.level) * self.yDistanceBetweenCardStartToNextStart() # based on the deck level
        return board.getY() + (board.getHeight() * self.y_MarginToBoardHeightRatio) + distanceFromTop

    def getImage(self):
        """
        Returns the image of the deck cover
        """
        if hasattr(self, 'image'):
            return self.image
        if self.color == Color.RED:
            return pygame.image.load('sprites/cards/red{}/back.png'.format(str(self.level)))
        else:
            return pygame.image.load('sprites/cards/{}/back.png'.format(self.color.name.lower()))

    def xDistanceBetweenCards(self):
        """
        Returns the distance between the end of the first card, and start of second card on the x-axis
        """
        return self.board.getWidth() * self.x_DistanceBetweenCardsToBoardWidthRatio

    def yDistanceBetweenCards(self):
        """
        Returns the distance between the end of the first card, and start of second card on the y-axis
        """
        return self.board.getHeight() * self.y_DistanceBetweenCardsToBoardHeightRatio

    def xDistanceBetweenCardStartToNextStart(self):
        """
        Returns the distance between the start of the first card, and start of second card on the x-axis
        """
        return self.xDistanceBetweenCards() + Card.getCardSize(self.board)[0]

    def yDistanceBetweenCardStartToNextStart(self):
        """
        Returns the distance between the start of the first card, and start of second card on the y-axis
        """
        return self.yDistanceBetweenCards() + Card.getCardSize(self.board)[1]

@Singleton
class BlueDeck(Deck):
    _NUMBER_OF_CARDS = 20
    _ID_START = 71 # 71-90

    def __init__(self):
        super().__init__(level=3, color=Color.BLUE)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.BLUE)
            self.cards.append(card)

@Singleton
class GreenDeck(Deck):
    _NUMBER_OF_CARDS = 40
    _ID_START = 1  # 1- 40

    def __init__(self):
        super().__init__(level=1, color=Color.GREEN)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.GREEN)
            self.cards.append(card)

@Singleton
class YellowDeck(Deck):
    _NUMBER_OF_CARDS = 30
    _ID_START = 41  # 41-70

    def __init__(self):
        super().__init__(level=2, color=Color.YELLOW)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.YELLOW)
            self.cards.append(card)

@Singleton
class RedDeck1(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 91

    def __init__(self):
        super().__init__(level=1, color=Color.RED)
        self.cardSlots = 2

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.RED)
            self.cards.append(card)

@Singleton
class RedDeck2(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 101

    def __init__(self):
        super().__init__(level=2, color=Color.RED)
        self.cardSlots = 2

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.RED)
            self.cards.append(card)

@Singleton
class RedDeck3(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 111

    def __init__(self):
        super().__init__(level=3, color=Color.RED)
        self.cardSlots = 2

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.RED)
            self.cards.append(card)
