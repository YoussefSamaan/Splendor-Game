from time import sleep
from board import Board
from flyweight import Flyweight
from color import Color
import pygame

@Flyweight
class Token:
    positions = {
        Color.WHITE: 0,
        Color.BLUE: 1,
        Color.GREEN: 2,
        Color.RED: 3,
        Color.BROWN: 4,
        Color.GOLD: 5,
    }

    multiplicities = {
        Color.WHITE: 7,
        Color.BLUE: 7,
        Color.GREEN: 7,
        Color.RED: 7,
        Color.BROWN: 7,
        Color.GOLD: 5,
    }

    yMargin = 31/33
    xMargin = 1/10
    xRatio =  1/15
    yRatio = 2/33
    xSeperationRatio = 1/20

    def __init__(self, color: Color, id: int):
        self._color = color
        self.image = pygame.image.load('sprites/tokens/{}.png'.format(color.name.lower()))
        self._id = id # Separates tokens with same color
        self.isOnDisplay = True

    
    def draw(self, screen, x, y):
        image = pygame.transform.scale(self.image, self.getSize())
        screen.blit(image, (x, y))
    
    def defaultPosition(self):
        board = Board.instance()
        width, height = self.getSize()
        x = board.getX() + board.getWidth() * self.xMargin + self.positions[self._color] * (width + board.getWidth() * self.xSeperationRatio)
        y = board.getY() + board.getHeight() * self.yMargin
        return (x, y)

    def getSize(self):
        board = Board.instance()
        width = board.getWidth() * self.xRatio
        height = board.getHeight() * self.yRatio
        return (width, height)

    def getColor(self):
        return self._color
    
    def getRect(self):
        return self.image.get_rect()

    def isClicked(self, mousePos):
        return self.getRect().collidepoint(mousePos)

    @staticmethod
    def initialize():
        id = 1
        for color in Token.positions.keys(): # for token color
            for _ in range(Token.multiplicities[color]): # for number of tokens
                Token.instance(color = color, id = id)
                id += 1

    @staticmethod
    def displayAll(screen):
        for token in Token._flyweights.values():
            if token.isOnDisplay: # can be improved by not drawing same color tokens on top of each other
                token.draw(screen, *token.defaultPosition())
