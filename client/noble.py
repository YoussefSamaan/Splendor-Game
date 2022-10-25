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
        self.slot = len(Noble._flyweights) # The slot position of the noble
        self.pos = self._defaultPosition()
        self.isOnDisplay = True

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
                noble.draw(screen, *noble._defaultPosition())

    @staticmethod
    def getClickedNoble(mousePos):
        """
        Returns the noble that is clicked. Returns None if no noble is clicked.
        """
        for noble in Noble._flyweights.values():
            if noble.isClicked(mousePos):
                return noble
        return None

    def takeNoble(self):
        self.isOnDisplay = False

    def draw(self, screen, x, y):
        screen.blit(pygame.transform.scale(self._image, Noble.getCardSize()), (x, y))

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

    def _defaultPosition(self):
        board = Board.instance()
        x = board.getWidth()*self.x_MarginToBoardSizeRatio + (self.slot) * Noble.getDistanceBetweenCards(board)
        x += board.getX()
        y = board.getHeight()*self.y_MarginToBoardSizeRatio
        y += board.getY()
        return (x, y)

    def isClicked(self, mousePos):
        """
        Returns True if the noble is clicked.
        :pre: self.pos is not None. This means that draw has to be called before this method.
        """
        xStart = self.pos[0]
        yStart = self.pos[1]
        xEnd = xStart + Noble.getCardSize()[0]
        yEnd = yStart + Noble.getCardSize()[1]
        return xStart <= mousePos[0] <= xEnd and yStart <= mousePos[1] <= yEnd
