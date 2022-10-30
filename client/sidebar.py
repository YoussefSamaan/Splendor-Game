import pygame
from singleton import Singleton
from card import Card
from noble import Noble
from board import Board
from utils import *

@Singleton
class Sidebar:
    def __init__(self, screenWidth, screenHeight):
        # [0] is width, [1] is height
        self.card_size = Card.get_card_size(Board.instance())
        self.noble_size = Noble.get_card_size()
        self.width = min(screenWidth/2, self.card_size[0])
        self.height = 10000 #min(screenHeight, 800)
        self.sidebar_rect = pygame.Rect(0, 0, self.width, self.height)
        # centered on the right side of the screen

        # ???????????
        self.sidebar_rect_center = (self.card_size[0]+self.noble_size[0]/2, screenHeight / 2)

        self.cards = {}
        self.reserved_cards = {}
        self.nobles = {}
 
        self.last_position_card = (0,self.card_size[0])
        self.last_position_noble = (0,self.card_size[0])
        self.last_position_reserved = (0,self.card_size[0])
        
        self.current_display = 0
        self.reserve_button = pygame.Rect(0, 10, self.width/3, self.card_size[1] / 4)
        self.nobles_button = pygame.Rect(self.width/3, 10, 
                                            self.card_size[0]/2, self.card_size[1] / 4)
        self.bought_button = pygame.Rect(self.width*2/3, 10,
                                            self.card_size[0]/2, self.card_size[1] / 4)
        

    def toggle1(self):
        if self.current_display == 0 or self.current_display == 2:
            self.current_display = 1
        else:
            self.current_display = 0
    
    def toggle2(self):
        if self.current_display == 0 or self.current_display == 1:
            self.current_display = 2
        else:
            self.current_display = 0


    def display(self, screen):
        """"""
        # screen.blit(self.sidebarImage, self.sidebarRect)
        
        # draw dummy color background
        pygame.draw.rect(screen, (0,0,0,0), self.sidebar_rect)
        if self.current_display == 0:

            for card in self.cards:
                card.draw(screen, self.cards[card][0], self.cards[card][1])
            self.draw_nobles_button(screen)
            self.draw_reserved_button(screen)
        elif self.current_display == 1:

            for noble in self.nobles:
                noble.draw(screen, self.nobles[noble][0], self.nobles[noble][1])
            self.draw_bought_button2(screen)
            self.draw_reserved_button(screen)
        else:

            for reserve_card in self.reserved_cards:
                reserve_card.draw(screen, self.reserved_cards[reserve_card][0], self.reserved_cards[reserve_card][1])   
            self.draw_bought_button(screen)
            self.draw_nobles_button(screen)
            
    def draw_nobles_button(self, surface: pygame.Surface):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        buy_button = button('Nobles', width=self.card_size[0]/2, height=self.card_size[1] / 4, color=LIGHT_GREY)
        x = 0
        y =  0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect

    def draw_reserved_button(self, surface: pygame.Surface):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        # this is a toggle button that switches between bought and reserved
        buy_button = button('Reserved', width=self.card_size[0]/2, height=self.card_size[1] / 4, color=LIGHT_GREY)
        x = self.card_size[0]/2
        y = 0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect

    def draw_bought_button(self, surface: pygame.Surface):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        # this is a toggle button that switches between bought and reserved
        buy_button = button('Bought', width=self.card_size[0]/2, height=self.card_size[1] / 4, color=LIGHT_GREY)
        x = self.card_size[0]/2
        y =  0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect
    
    def draw_bought_button2(self, surface: pygame.Surface):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        # this is a toggle button that switches between bought and reserved
        buy_button = button('Bought', width=self.card_size[0]/2, height=self.card_size[1] / 4, color=LIGHT_GREY)
        x = 0
        y =  0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect

    def add_noble(self, noble):
        self.nobles[noble] = self.last_position_noble
        self.last_position_noble = (self.last_position_noble[0], self.last_position_noble[1]+self.noble_size[1])

    def add_card(self, card):
        self.cards[card] = self.last_position_card
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1]+self.card_size[1])

    def reserve_card(self, reserved):
        self.reserved_cards[reserved] = self.last_position_reserved
        self.last_position_reserved = (self.last_position_reserved[0], self.last_position_reserved[1]+self.card_size[1])
        
    def update_positions(self, amount):
        # updating the last values for new cards
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
        # getting the first value of the dict and its y position
        # if (direction < 0 and list(self.cards.items())[0][1][1] < 50) :
        #     pass
        # elif (direction > 0 and self.last_position_card[1] > Board.instance().height):
        #     pass
        # else:
        #     self.update_positions(direction)
        #     self.sidebarRect.move_ip(0, direction)
        self.update_positions(direction)
        self.sidebar_rect.move_ip(0, direction)
    
    def get_rect(self):
        return self.sidebarRect

    def get_x(self):
        return self.sidebarRect.x
    
    def get_y(self):
        return self.sidebarRect.y
    
    def get_width(self):
        return self.width

    def get_height(self):
        return self.height
    
    def get_center(self):
        return self.sidebarRect.center
    
    def is_clicked_toggle(self, mousePos):
        if self.reserve_button.collidepoint(mousePos) :
            return 1
        if self.nobles_button.collidepoint(mousePos):
            return 2