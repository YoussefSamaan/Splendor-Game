from board import Board
from flyweight import Flyweight
import pygame
from cost import Cost

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

    def draw(self, screen):
        board = Board.instance()
        x = board.getWidth()*self.x_MarginToBoardSizeRatio + (self._id) * Noble.getClass().getDistanceBetweenCards(board)
        x += board.getX()
        y = board.getHeight()*self.y_MarginToBoardSizeRatio
        y += board.getY()
        screen.blit(pygame.transform.scale(self._image, Noble.getClass().getCardSize(board)), (x, y))

    def getRect(self):
        return self._image.get_rect()

    def getPrestigePoints(self):
        return self._prestigePoints

    def getCost(self):
        return self._cost

    def getId(self):
        return self._id

    def getCardSize(board):
        width = board.getWidth() * Noble.getClass().x_ratio
        height = board.getHeight() * Noble.getClass().y_ratio
        return (width, height)
    
    def getDistanceBetweenCards(board):
        return (board.getWidth() * Noble.getClass().x_DistanceBetweenCardsToBoardWidthRatio + Noble.getClass().getCardSize(board)[0])
