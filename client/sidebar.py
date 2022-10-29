import pygame
from singleton import Singleton
from card import Card
from board import Board

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
        self.cards = {}
        self.reservedCards = {}
        self.tokens = []
        self.nobles = {}

        self.card_size = Card.get_card_size(Board.instance())

        self.last_position_card = (0,0)
        self.last_position_noble = (0,0)
        self.last_position_reserved = (0,0)
        

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
        self.nobles[noble] = self.last_position_noble

    def add_card(self, card):
        self.cards[card] = self.last_position_card
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1]+self.card_size[1])

    def reserve_card(self, reserved):
        self.nobles[reserved] = self.last_position_reserved
    
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