from time import sleep
from board import Board
from color import Color
import pygame

class Token:
    positions = {
        Color.WHITE: 0,
        Color.BLUE: 1,
        Color.GREEN: 2,
        Color.RED: 3,
        Color.BROWN: 4,
        Color.GOLD: 5,
    }

    yMargin = 31/33
    xMargin = 1/10
    xRatio =  1/15
    yRatio = 2/33
    xSeperationRatio = 1/20

    def __init__(self, color: Color):
        self._color = color
        self.image = pygame.image.load('sprites/tokens/{}.png'.format(color.name.lower()))
    
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
    def initialize(screen):
        for color in Token.positions.keys():
            Token(color).draw(screen, *Token(color).defaultPosition())
            pygame.display.update()
            sleep(0.5)
