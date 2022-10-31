import time

import pygame

from action import Action
from board import Board
from bonus import Bonus
from color import Color
from cost import Cost
from flyweight import Flyweight
from utils import *


def draw_reserve_button(surface: pygame.Surface):
    """
    The x and y coordinates returned are relative to the surface.
    :param surface:
    :return:
    """
    reserve_button = button('Reserve', width=surface.get_width() / 6, height=surface.get_height() / 8, color=RED)
    x = surface.get_width() / 2 + 10
    y = surface.get_height() - reserve_button.get_height() * 1.5
    surface.blit(reserve_button, (x, y))
    button_rect = reserve_button.get_rect()
    button_rect.x = x
    button_rect.y = y
    return button_rect


def draw_buy_button(surface: pygame.Surface):
    """
    The x and y coordinates returned are relative to the surface.
    :param surface:
    :return:
    """
    buy_button = button('Buy', width=surface.get_width() / 6, height=surface.get_height() / 8, color=GREEN)
    x = surface.get_width() / 2 - buy_button.get_width() * 1.5 - 10
    y = surface.get_height() - buy_button.get_height() * 1.5
    surface.blit(buy_button, (x, y))
    button_rect = buy_button.get_rect()
    button_rect.x = x
    button_rect.y = y
    return button_rect


@Flyweight
class Card:
    x_ratio = 0.09  # ratio of card width to board width
    y_ratio = 0.18  # ratio of card height to board height

    def __init__(self, id: int, deck, prestige_points=1, cost=Cost(1, 1, 1, 1, 1),
                 bonus=Bonus(1, 1, 1, 1, 1)):
        self._id = id
        self._presetge_points = prestige_points
        self._cost = cost
        self._bonus = bonus
        self._color = deck.get_color()
        self.deck = deck
        self._image = self._get_image()
        self.pos = None

    @staticmethod
    def get_card_size(board):
        width = board.get_width() * Card.x_ratio
        height = board.get_height() * Card.y_ratio
        return width, height

    def draw(self, screen, x, y):
        board = Board.instance()
        width, height = Card.get_card_size(board)
        image = pygame.transform.scale(self._image, (int(width), int(height)))
        screen.blit(image, (x, y))
        self.pos = (x, y)

    def is_clicked(self, mousePos):
        """
        Returns True if the card is clicked.
        :pre: self.pos is not None. This means that draw has to be called before this method.
        """
        x_start = self.pos[0]
        y_start = self.pos[1]
        x_end = x_start + Card.get_card_size(Board.instance())[0]
        y_end = y_start + Card.get_card_size(Board.instance())[1]
        return x_start <= mousePos[0] <= x_end and y_start <= mousePos[1] <= y_end

    def get_rect(self):
        return self._image.get_rect()

    def get_color(self):
        return self._color

    def get_id(self):
        return self._id

    def get_deck(self):
        return self.deck

    def set_pos(self, x, y):
        self.pos = (x, y)
        self._image.get_rect().center = self.pos

    def _get_image(self):
        if self._color == Color.RED:
            # Look inside all the red card folders
            for i in range(1, 4):
                try:
                    return pygame.image.load('../sprites/cards/red{}/{}.png'.format(i, self._id))
                except:
                    pass

        return pygame.image.load('../sprites/cards/{}/{}.png'.format(self._color.name.lower(), self._id))

    def get_user_selection(self, screen) -> Action:
        """
        Shows a box to the user with all the card's information.
        Allows user to choose whether to buy or reserve the card.
        """
        selection_box, selection_box_rect = get_selection_box(screen)
        # draw the card's prestige points on the left side of the rect
        font = pygame.font.SysFont('comicsans', 40)
        text = font.render(str(self._presetge_points), 1, (0, 0, 0))
        selection_box.blit(text, (selection_box_rect.x + 10, selection_box_rect.y + 10))
        # # draw the card's cost on the right side of the rect
        # self._cost.draw(screen, rect.x + rect.width - 10, rect.y + 10)
        # # draw the card's bonus on the bottom of the rect
        self.draw(selection_box, selection_box_rect.width / 2 - Card.get_card_size(Board.instance())[0] / 2,
                  selection_box_rect.height / 2 - Card.get_card_size(Board.instance())[1] / 2)

        # draw the reserve button
        reserve_button = draw_reserve_button(selection_box)
        # get the true position of the button
        reserve_button.x += selection_box_rect.x
        reserve_button.y += selection_box_rect.y
        # draw the buy button
        buy_button = draw_buy_button(selection_box)
        # get the true position of the button
        buy_button.x += selection_box_rect.x
        buy_button.y += selection_box_rect.y

        screen.blit(selection_box, selection_box_rect)
        pygame.display.update()
        # wait for user to click on a button
        while True:
            for event in pygame.event.get():
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if reserve_button.collidepoint(event.pos):
                        return Action.RESERVE
                    elif buy_button.collidepoint(event.pos):
                        return Action.BUY
                    else:
                        return Action.CANCEL
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        return Action.CANCEL
                elif event.type == pygame.QUIT:
                    pygame.quit()
                    quit()

    def buy(self):
        # FIXME: Implement this to put card in player inventory
        self.deck.take_card(self)

    def reserve(self):
        # FIXME: Implement this to put card in player inventory
        self.deck.take_card(self)