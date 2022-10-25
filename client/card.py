from board import Board
from flyweight import Flyweight
import pygame
from cost import Cost
from bonus import Bonus
from color import Color

@Flyweight
class Card:
    x_ratio = 0.09 # ratio of card width to board width
    y_ratio = 0.12 # ratio of card height to board height

    def __init__(self, prestigePoints: int, cost: Cost, bonus: Bonus, color: Color, id: int):
        self._prestigePoints = prestigePoints
        self._cost = cost
        self._bonus = bonus
        self._color = color
        self._id = id
        self._image = self._getImage()
        self.pos = None

    def draw(self, screen, x, y):
        board = Board.instance()
        width, height = Card.getCardSize(board)
        image = pygame.transform.scale(self._image, (int(width), int(height)))
        screen.blit(image, (x, y))
        self.pos = (x, y)

    def getRect(self):
        return self._image.get_rect()

    def getPrestigePoints(self):
        return self._prestigePoints

    def getCost(self):
        return self._cost
    
    def getBonus(self):
        return self._bonus

    def getColor(self):
        return self._color

    def getId(self):
        return self._id

    def setPos(self, x, y):
        self.pos = (x, y)
        self._image.get_rect().center = self.pos

    def getCardSize(board):
        width = board.getWidth() * Card.x_ratio
        height = board.getHeight() * Card.y_ratio
        return (width, height)

    def _getImage(self):
        if self._color == Color.RED:
            # Look inside all the red card folders
            for i in range(1, 4):
                try:
                    return pygame.image.load('sprites/cards/red{}/{}.png'.format(i, self._id))
                except:
                    pass
        
        return pygame.image.load('sprites/cards/{}/{}.png'.format(self._color.name.lower(), self._id))
