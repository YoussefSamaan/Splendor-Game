from board import Board
from flyweight import Flyweight
import pygame
from cost import Cost
import random

@Flyweight
class Noble:
    x_MarginToBoardSizeRatio = 0.2
    y_MarginToBoardSizeRatio = 0.3
    x_DistanceBetweenCardsToBoardWidthRatio = 1 / 30
    x_ratio = 1/12 # ratio of card width to board width 
    y_ratio = 1/12 # ratio of card height to board height

    def __init__(self, prestigePoints, cost: Cost, id: int):
        self._prestigePoints = prestigePoints
        self._cost = cost
        self._id = id # 1 -> 4
        self._image = pygame.image.load('sprites/nobles/{}.png'.format(id))
        self._pos = len(Noble._flyweights) # The slot position of the noble
        self.isOnDisplay = True

    def draw(self, screen, x, y):
        screen.blit(pygame.transform.scale(self._image, Noble.getCardSize()), (x, y))

    def defaultPosition(self):
        board = Board.instance()
        x = board.getWidth()*self.x_MarginToBoardSizeRatio + (self._pos) * Noble.getDistanceBetweenCards(board)
        x += board.getX()
        y = board.getHeight()*self.y_MarginToBoardSizeRatio
        y += board.getY()
        return (x, y)

    def getRect(self):
        return self._image.get_rect()

    def getPrestigePoints(self):
        return self._prestigePoints

    def getCost(self):
        return self._cost

    def getId(self):
        return self._id

    def getCardSize():
        board = Board.instance()
        width = board.getWidth() * Noble.x_ratio
        height = board.getHeight() * Noble.y_ratio
        return (width, height)
    
    def getDistanceBetweenCards(board):
        return (board.getWidth() * Noble.x_DistanceBetweenCardsToBoardWidthRatio + Noble.getCardSize()[0])

    @staticmethod
    def initialize(n):
        ids = random.sample(range(1, 11), n)
        # Create n nobles with the chosen ids
        for id in ids:
            Noble.instance(prestigePoints = 3, cost = Cost(1,1,1,1,1), id = id)

    @staticmethod
    def displayAll(screen):
        for noble in Noble._flyweights.values():
            if noble.isOnDisplay:
                noble.draw(screen, *noble.defaultPosition())
