import pygame
from singleton import Singleton

@Singleton
class Board:
    def __init__(self, screenWidth, screenHeight):
        # self.boardImage = pygame.image.load('sprites/board.png')
        self.width = min(screenWidth, 1000)
        self.height = min(screenHeight, 1000)
        # self.boardImage = pygame.transform.scale(self.boardImage, (self.width, self.height))
        self.boardRect = pygame.Rect(0, 0, self.width, self.height)
        self.boardRect.center = (screenWidth / 2, screenHeight / 2)

    def draw(self, screen):
        # screen.blit(self.boardImage, self.boardRect)
        
        # draw black background
        pygame.draw.rect(screen, (0, 0, 0), self.boardRect)
    
    def getRect(self):
        return self.boardRect

    def getX(self):
        return self.boardRect.x
    
    def getY(self):
        return self.boardRect.y
    
    def getWidth(self):
        return self.width

    def getHeight(self):
        return self.height
    
    def getCenter(self):
        return self.boardRect.center
    
    def isClicked(self, mousePos):
        return self.boardRect.collidepoint(mousePos)
