from flyweight import Flyweight
import pygame
from cost import Cost
from bonus import Bonus
from color import Color

@Flyweight
class Card:
    width = 50
    height = 70

    def __init__(self, prestigePoints: int, cost: Cost, bonus: Bonus, color: Color, id: int):
        self._prestigePoints = prestigePoints
        self._cost = cost
        self._bonus = bonus
        self._color = color
        self._id = id
        self._image = pygame.image.load('images/card.png') # Temporary, use next line when all images are ready
        # self._image = pygame.image.load('images/cards/' + str(self._id) + '.png')  
        self._image = pygame.transform.scale(self._image, (self.width, self.height))

    def draw(self, screen, x, y):
        screen.blit(self._image, (x, y))
    
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
