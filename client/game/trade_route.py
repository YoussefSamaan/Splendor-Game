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
        self.trade_route_button = pygame.Rect(self.width * 3 / 4, self.height * 3/4,
                                self.width / 8, self.card_size[1] / 4)
        
    def display(self, screen):
        """displays the trade route button"""
        pygame.draw.rect(screen, Color.BLACK, self.trade_route_button)
        outlined_text(screen, "Trade Routes", Color.WHITE, self.trade_route_button.center)

    def open_trade_route_menu(self):
        """opens the trade route menu after clicking on button"""
        pass