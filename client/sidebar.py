import pygame
from singleton import Singleton
from card import Card
from noble import Noble
from board import Board
from utils import *

@Singleton
class Sidebar:
    def __init__(self, screen_width, screen_height):
        # [0] is width, [1] is height
        self.card_size = Card.get_card_size(Board.instance())
        self.noble_size = Noble.get_card_size()
        self.width = screen_width  - Board.instance().get_rect().width
        self.height = 10000 #min(screenHeight, 800)
        self.sidebar_rect = pygame.Rect(0, 0, self.width, self.height)

        self.cards = {}
        self.reserved_cards = {}
        self.nobles = {}
 
        self.last_position_card = (0,self.card_size[0])
        self.last_position_noble = (0,self.card_size[0])
        self.last_position_reserved = (0,self.card_size[0])
        
        self.current_display = 0
        self.bought_button = pygame.Rect(0, 0,
                                            self.width/3, self.card_size[1] / 4)
        self.nobles_button = pygame.Rect(self.width/3, 0, 
                                            self.width/3, self.card_size[1] / 4)
        self.reserve_button = pygame.Rect(self.width*2/3, 0, 
                                            self.width/3, self.card_size[1] / 4)
        
        self.display_color_active = LIGHT_BLUE
        

    def toggle(self, num):
        if type(num) !=  int:
            return
        elif -1 < num < 4:
            self.current_display = num


    def display(self, screen):
        # draw dummy color background
        pygame.draw.rect(screen, (0,0,0,0), self.sidebar_rect)
        if self.current_display == 0:
            for card in self.cards:
                card.draw(screen, self.cards[card][0]*1.5, self.cards[card][1]*1.5)
        elif self.current_display == 1:
            for noble in self.nobles:
                noble.draw(screen, self.nobles[noble][0], self.nobles[noble][1])
        else:
            for reserve_card in self.reserved_cards:
                reserve_card.draw(screen, self.reserved_cards[reserve_card][0], self.reserved_cards[reserve_card][1])   

        self.draw_buttons(screen)

    def draw_buttons(self, screen):
        if self.current_display == 1:
            self.draw_nobles_button(screen, self.display_color_active)
            self.draw_reserved_button(screen)
            self.draw_bought_button(screen)
        elif self.current_display == 2:
            self.draw_nobles_button(screen)
            self.draw_reserved_button(screen, self.display_color_active)
            self.draw_bought_button(screen)
        else:
            self.draw_nobles_button(screen)
            self.draw_reserved_button(screen)
            self.draw_bought_button(screen, self.display_color_active)
                    
    def draw_nobles_button(self, surface: pygame.Surface, color=LIGHT_GREY):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        buy_button = button('Nobles', width=self.width/3, height=self.card_size[1] / 4, color=color)
        x = self.width/3
        y =  0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect

    def draw_reserved_button(self, surface: pygame.Surface, color=LIGHT_GREY):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        # this is a toggle button that switches between bought and reserved
        buy_button = button('Reserved', width=self.width/3, height=self.card_size[1] / 4, color=color)
        x = self.width*2/3
        y = 0
        surface.blit(buy_button, (x, y))
        button_rect = buy_button.get_rect()
        button_rect.x = x
        button_rect.y = y
        return button_rect

    def draw_bought_button(self, surface: pygame.Surface, color=LIGHT_GREY):
        """
        The x and y coordinates returned are relative to the surface.
        :param surface:
        :return:
        """
        buy_button = button('Bought', width=self.width/3, height=self.card_size[1] / 4, color=color)
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
    
    def is_clicked_toggle(self, mousePos):
        if self.reserve_button.collidepoint(mousePos) :
            return 2
        elif self.nobles_button.collidepoint(mousePos):
            return 1
        elif self.bought_button.collidepoint(mousePos):
            return 0