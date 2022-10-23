from flyweight import Flyweight
import pygame

@Flyweight
class Noble:
    width = 50
    height = 70

    def __init__(self, prestigePoints, cost: Cost, id: int):
        self._prestigePoints = prestigePoints
        self._cost = cost
        self._id = id
        self._image = pygame.image.load('images/noble.png')
        self._image = pygame.transform.scale(self._image, (self.width, self.height))

    def draw(self, screen, x, y):
        screen.blit(self._image, (x, y))

    def getRect(self):
        return self._image.get_rect()

    def getPrestigePoints(self):
        return self._prestigePoints

    def getCost(self):
        return self._cost

    def getId(self):
        return self._id
