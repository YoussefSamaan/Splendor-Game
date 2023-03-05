import pygame

from board import Board
from card import Card
from color import Color
from sidebar import Sidebar
from singleton import Singleton
from utils import outlined_text

@Singleton
class TradeRoute:
    def __init__(self):
        self.card_size = Card.get_card_size()
        self.width = Board.instance().get_rect().width
        self.height = Board.instance().get_rect().height
        # rect: (x, y, width, height)
        self.trade_route_button = pygame.Rect(self.width * 1.1, self.height * 17/20,
                                self.width / 8, self.card_size[1] / 4)
        
        self.trade_route_menu = self._get_trade_routes_image()
        self.coat_of_arms_list = []
        for player in range(0,3):
            self.coat_of_arms_list.append(self._get_coat_image(player))

    def check_click(self, pos, screen):
        """checks if the trade route button is clicked"""
        if self.trade_route_button.collidepoint(pos):
            self.open_trade_route_menu(screen)
        
        
    def display(self, screen):
        """displays the trade route button"""
        pygame.draw.rect(screen, (0,0,0,0), self.trade_route_button)
        outlined_text(screen, text="Trade Routes", center=self.trade_route_button.center)

    def open_trade_route_menu(self, screen):
        """opens the trade route menu after clicking on button"""
        screen.blit(self.trade_route_menu)
        pass
    


    def _get_trade_routes_image(self):
        """
        Returns the coat with coat_id [0,3]
        """
        return pygame.image.load(f'../sprites/trade_route/board_extension.png')

    def _get_coat_image(self, coat_id: int):
        """
        Returns the coat with coat_id [0,3]
        """
        return pygame.image.load(f'../sprites/trade_route/coat{coat_id}.png')