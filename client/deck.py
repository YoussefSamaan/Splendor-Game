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
        self.posToDrawAt = 0 # The card slot to draw at (1-4)
        self.cardSlots = 4
        self.level = level
        self.color = color
        self.image = pygame.image.load('sprites/cards/{}/back.png'.format(color.name.lower()))
        # self.image = pygame.transform.scale(self.image, (self.width, self.height))
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
        x = board.getX() + board.getWidth() * self.x_MarginToBoardWidthRatio
        y = board.getY() + (board.getHeight() * self.y_MarginToBoardHeightRatio) + (3 - self.level) * (height + board.getHeight() * self.y_DistanceBetweenCardsToBoardHeightRatio)
        self.image = pygame.transform.scale(self.image, (int(width), int(height)))
        screen.blit(self.image, (x, y))
        for _ in range(self.cardSlots):
            card = self.cards.pop()
            self.drawCardToBoard(card, screen)
            pygame.display.update()
            sleep(0.5)

    def drawCardToBoard(self, card, screen):
        """
        Draws a card on the board surface
        """
        board = Board.instance()
        x = self.getDeckX(board) + (self.posToDrawAt + 1) * (board.getWidth() * self.x_DistanceBetweenCardsToBoardWidthRatio + Card.getClass().getCardSize(board)[0])
        y = self.getDeckY(board)
        card.draw(screen, x, y)
        self.posToDrawAt = (self.posToDrawAt + 1) % (self.cardSlots)

    @staticmethod
    def getDeckDisplaySize(board):
        """
        Returns the size of the deck cover based on the size of the board
        """
        return Card.getClass().getCardSize(board)

    def getDeckPos(self, board):
        """
        Returns the position of the deck cover based on the size of the board
        """
        return (self.getDeckX(board), self.getDeckY(board))
    
    def getDeckX(self, board):
        """
        Returns the x-coordinate of the deck cover based on the size of the board
        """
        return board.getX() + board.getWidth() * self.x_MarginToBoardWidthRatio

    def getDeckY(self, board):
        """
        Returns the y-coordinate of the deck cover based on the size of the board
        """
        height = Deck.getDeckDisplaySize(board)[1]
        return board.getY() + (board.getHeight() * self.y_MarginToBoardHeightRatio) + (3 - self.level) * (height + board.getHeight() * self.y_DistanceBetweenCardsToBoardHeightRatio)
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
    _ID_START = 1 # 1- 40

    def __init__(self):
        super().__init__(level=1, color=Color.GREEN)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.GREEN)
            self.cards.append(card)

@Singleton
class YellowDeck(Deck):
    _NUMBER_OF_CARDS = 30
    _ID_START = 41 # 41-70

    def __init__(self):
        super().__init__(level=2, color=Color.YELLOW)

    def initDeck(self):
        for i in range(self._NUMBER_OF_CARDS):
            card = Card.instance(id=self._ID_START + i, prestigePoints=3, cost=Cost(0, 0, 0, 0, 0), bonus=Bonus(0, 0, 0, 0, 0), color=Color.YELLOW)
            self.cards.append(card)