import pygame
from singleton import Singleton
@Singleton
class Sidebar:
    def __init__(self, screenWidth, screenHeight):
        # self.sidebarImage = pygame.image.load('sprites/sidebar.png')
        self.width = min(screenWidth/4, 400)
        self.height = min(screenHeight, 800)
        # self.sidebarImage = pygame.transform.scale(self.sidebarImage, (self.width, self.height))
        self.sidebarRect = pygame.Rect(0, 0, self.width, self.height)
        # centered on the right side of the screen
        self.sidebarRect.center = (screenWidth / 8, screenHeight / 2)
        self.cards = []
        self.reservedCards = []
        self.tokens = []
        self.nobles = []

    def display(self, screen):
        # screen.blit(self.sidebarImage, self.sidebarRect)
        
        # draw dummy color background
        pygame.draw.rect(screen, (50, 50, 50), self.sidebarRect)
        for card in self.cards:
            card.draw(screen, *card.pos)
        # for card in self.reservedCards:
        #     card.draw(screen, *card.pos)
        # for token in self.tokens:
        #     token.draw(screen, *token.pos)
        # for noble in self.nobles:
        #     noble.draw(screen, *noble.pos)


    def add_noble(self, noble):
        self.nobles.append(noble)

    def add_card(self, card):
        self.cards.append(card)

    def reserve_card(self, card):
        self.reservedCards.append(card)
    
    def add_token(self, token):
        self.tokens.append(token)

    def scroll_sidebar(self, direction):
        pass
    
    def getRect(self):
        return self.sidebarRect

    def getX(self):
        return self.sidebarRect.x
    
    def getY(self):
        return self.sidebarRect.y
    
    def getWidth(self):
        return self.width

    def getHeight(self):
        return self.height
    
    def getCenter(self):
        return self.sidebarRect.center
    
    def isClicked(self, mousePos):
        return self.sidebarRect.collidepoint(mousePos)