import pygame
from singleton import Singleton
from card import Card
from noble import Noble
from board import Board

@Singleton
class Sidebar:
    def __init__(self, screenWidth, screenHeight):
                # [0] is width, [1] is height
        self.card_size = Card.get_card_size(Board.instance())
        self.noble_size = Noble.get_card_size()
        # self.sidebarImage = pygame.image.load('sprites/sidebar.png')
        self.width = min(screenWidth/2, 2*self.card_size[0]+self.noble_size[0])
        self.height = min(screenHeight, 800)
        # self.sidebarImage = pygame.transform.scale(self.sidebarImage, (self.width, self.height))
        self.sidebarRect = pygame.Rect(0, 0, self.width, self.height)
        # centered on the right side of the screen
        self.sidebarRect.center = (self.card_size[0]+self.noble_size[0]/2, screenHeight / 2)
        self.cards = {}
        self.reserved_cards = {}
        self.nobles = {}

        self.last_position_card = (0,0)
        self.last_position_noble = (self.card_size[0],0)
        self.last_position_reserved = (self.card_size[0]+self.noble_size[0],0)
        

    def display(self, screen):
        # screen.blit(self.sidebarImage, self.sidebarRect)
        
        # draw dummy color background
        pygame.draw.rect(screen, (50, 50, 50), self.sidebarRect)
        
        for card in self.cards:
            card.draw(screen, self.cards[card][0], self.cards[card][1])
        for noble in self.nobles:
            noble.draw(screen, self.nobles[noble][0], self.nobles[noble][1])
        for reserve_card in self.reserved_cards:
            reserve_card.draw(screen, self.reserved_cards[reserve_card][0], self.reserved_cards[reserve_card][1])   
            
    def add_noble(self, noble):
        self.nobles[noble] = self.last_position_noble
        self.last_position_noble = (self.last_position_noble[0], self.last_position_noble[1]+self.noble_size[1])

    def add_card(self, card):
        self.cards[card] = self.last_position_card
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1]+self.card_size[1])

    def reserve_card(self, reserved):
        self.reserved_cards[reserved] = self.last_position_reserved
        self.last_position_reserved = (self.last_position_reserved[0], self.last_position_reserved[1]+self.card_size[1])
        
    
    def add_token(self, token):
        self.tokens.append(token)
    def update_positions(self, amount):
        # updating the last valuse for new cards
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1] + amount)
        self.last_position_noble = (self.last_position_noble[0], self.last_position_noble[1] + amount)
        self.last_position_reserved =(self.last_position_reserved[0], self.last_position_reserved[1] + amount)
        # updating values of cards in dict
        for item in self.cards:
            self.cards[item] = (self.cards[item][0], self.cards[item][1] + amount)
        for item in self.nobles:
            self.nobles[item] = (self.nobles[item][0], self.nobles[item][1] + amount)
        for item in self.reserved_cards:
            self.reserved_cards[item] = (self.reserved_cards[item][0], self.reserved_cards[item][1] + amount)


    def scroll_sidebar(self, direction):
        #print("scrolling")
        # update last positions
        # update positions in dicts
        self.update_positions(direction)
        self.sidebarRect.move_ip(0, direction)
    
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