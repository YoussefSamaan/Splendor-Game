from random import shuffle
from time import sleep
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
        self.posToDrawAt = 0 # The card slot to draw at (1-cardSlots)
        self.cardSlots = 4
        self.level = level
        self.color = color
        self.image = self.getImage()
        self.rect = self.image.get_rect()
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

        :pre: The deck is not empty
        :return: The card that was drawn
        """
        card = self.cards.pop()
        self.drawCardToBoard(card, screen)
        return card

    def draw(self, screen):
        """
        Draws the deck cover to the board surface.
        Fills the slots for the deck with cards
        """
        board = Board.instance()
        width, height = Deck.getDeckDisplaySize(board)
        x = self.getDeckX(board)
        if self.color == Color.RED:
            x -= width
        y = self.getDeckY(board)
        image = pygame.transform.scale(self.image, (int(width), int(height)))
        screen.blit(image, (x, y))
        pygame.display.update()
        sleep(0.5)
        for _ in range(self.cardSlots):
            self.drawCard(screen)
            pygame.display.update()
            sleep(0.5)

    def drawCardToBoard(self, card, screen):
        """
        Draws a card on the board surface
        """
        board = Board.instance()
        card.draw(screen, *self.getPosOfNextCard(board))
        self.posToDrawAt = (self.posToDrawAt + 1) % (self.cardSlots)

    def getPosOfNextCard(self, board):
        """
        Returns the position of the next card to be drawn
        """
        if self.color == Color.RED:
            x = self.getDeckX(board) - (self.posToDrawAt + 1) * self.xDistanceBetweenCardStartToNextStart(board) - Card.getCardSize(board)[0]
        else:
            x = self.getDeckX(board) + (self.posToDrawAt + 1) * self.xDistanceBetweenCardStartToNextStart(board)
        y = self.getDeckY(board)
        return (x, y)

    @staticmethod
    def getDeckDisplaySize(board):
        """
        Returns the size of the deck cover based on the size of the board
        """
        return Card.getCardSize(board)

    def getDeckPos(self, board):
        """
        Returns the position of the deck cover based on the size of the board
        """
        return (self.getDeckX(board), self.getDeckY(board))
    
    def getDeckX(self, board):
        """
        Returns the x-coordinate of the side of the deck nearest to the board based on the size of the board
        """
        if self.color == Color.RED:
            return board.getX() + board.getWidth()*(1 - self.x_MarginToBoardWidthRatio) # Red deck is on the right side of the board

        return board.getX() + board.getWidth() * self.x_MarginToBoardWidthRatio

    def getDeckY(self, board):
        """
        Returns the y-coordinate of the deck cover based on the size of the board
        """
        distanceFromTop = (3 - self.level) * self.yDistanceBetweenCardStartToNextStart(board) # based on the deck level
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

    def xDistanceBetweenCards(self, board):
        """
        Returns the distance between the end of the first card, and start of second card on the x-axis
        """
        return board.getWidth() * self.x_DistanceBetweenCardsToBoardWidthRatio

    def yDistanceBetweenCards(self, board):
        """
        Returns the distance between the end of the first card, and start of second card on the y-axis
        """
        return board.getHeight() * self.y_DistanceBetweenCardsToBoardHeightRatio

    def xDistanceBetweenCardStartToNextStart(self, board):
        """
        Returns the distance between the start of the first card, and start of second card on the x-axis
        """
        return self.xDistanceBetweenCards(board) + Card.getCardSize(board)[0]

    def yDistanceBetweenCardStartToNextStart(self, board):
        """
        Returns the distance between the start of the first card, and start of second card on the y-axis
        """
        return self.yDistanceBetweenCards(board) + Card.getCardSize(board)[1]

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
