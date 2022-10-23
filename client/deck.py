from random import shuffle
from singleton import Singleton
from color import Color
from bonus import Bonus
from card import Card
from cost import Cost
import pygame


class Deck:

    width = 50
    height = 70
    distanceBetweenCards = 30

    def __init__(self, level: int, color: Color):
        """
        Initializes the deck
        """
        self.posToDrawAt = 0 # The card slot to draw at (1-4)
        self.level = level
        self.color = color
        # self.image = pygame.image.load('images/{}Deck.png'.format(color.name.lower()))
        self.image = pygame.image.load('images/card.png')
        self.image = pygame.transform.scale(self.image, (self.width, self.height))
        self.rect = self.image.get_rect()
        self.rect.center = self._CENTER
        self.cards = []
        self.initDeck()
        self.shuffle()

    def shuffle(self):
        """
        Shuffles the deck
        """
        shuffle(self.cards)


    def drawCard(self, screen) -> Card:
        """
        Draws a card from the deck, displays it on the board, and returns it
        """
        card = self.cards.pop()
        self.drawCardToBoard(card, screen)
        return card

    def draw(self, screen):
        """
        Draws the deck to the screen
        """
        screen.blit(self.image, self.rect)

    def drawCardToBoard(self, card, screen):
        """
        Draws a card to the board
        """
        card.draw(screen, self._CENTER[0] + (self.posToDrawAt + 1)*(Card.width + self.distanceBetweenCards), self._CENTER[1])
        self.posToDrawAt = (self.posToDrawAt + 1) % 4

@Singleton
class BlueDeck(Deck):
    _NUMBER_OF_CARDS = 20
    _ID_START = 71 # 71-90
    _CENTER = (100, 100)

    def __init__(self):
        super().__init__(level=1, color=Color.BLUE)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.BLUE)
            self.cards.append(card)

@Singleton
class GreenDeck(Deck):
    _NUMBER_OF_CARDS = 40
    _ID_START = 1 # 1- 40
    _CENTER = (100, 200)

    def __init__(self):
        super().__init__(level=1, color=Color.GREEN)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.BLUE)
            self.cards.append(card)

@Singleton
class YellowDeck(Deck):
    _NUMBER_OF_CARDS = 30
    _ID_START = 41 # 41-70
    _CENTER = (100, 300)

    def __init__(self):
        super().__init__(level=2, color=Color.YELLOW)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.BLUE)
            self.cards.append(card)