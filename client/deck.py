from random import shuffle
from typing import Dict
from xmlrpc.client import boolean

import pygame

from board import Board
from bonus import Bonus
from card import Card
from color import Color
from cost import Cost
from singleton import Singleton


class Deck:
    x_DistanceBetweenCardsToBoardWidthRatio = 1 / 30  # The ratio of the distance between cards on the x-axis to the width of the board
    y_DistanceBetweenCardsToBoardHeightRatio = 1 / 22  # The ratio of the distance between cards on the y-axis to the height of the board
    x_MarginToBoardWidthRatio = 1 / 60  # The margin between the left side of the board and the left side of the deck cover
    y_MarginToBoardHeightRatio = 0.44  # First deck starts at 0.44 * board height y-coordinate
    _ID_START = 0
    _NUMBER_OF_CARDS = 0

    def __init__(self, level: int, color: Color, card_slots: int = 4):
        """
        Initializes the deck
        """
        self.card_slots = card_slots
        self.level = level
        self.color = color
        self.image = self._get_image()
        self.rect = self.image.get_rect()
        self.card_ids = []
        self.cardsOnDisplay: Dict[int, Card] = {}
        self.board = Board.instance()
        self._init_deck()
        self._shuffle()
        self._fill_empty_slots()

    @staticmethod
    def get_deck_display_size(board):
        """
        Returns the size of the deck cover based on the size of the board
        """
        return Card.get_card_size(board)

    def is_empty(self):
        """
        Returns whether the deck is empty
        """
        return len(self.card_ids) == 0

    def draw_card(self) -> Card:
        """
        Draws a card from the deck

        :pre: The deck is not empty
        :return: The card that was drawn
        """
        assert not self.is_empty(), "Deck is empty"
        id = self.card_ids.pop()
        card = Card.instance(id=id, deck=self)
        self._add_card_to_display(card)
        return card

    def display(self, screen):
        """
        Displays the cards on the board, and the deck cover
        """
        self._display_deck_cover(screen)
        for slot_pos, card in self.cardsOnDisplay.items():
            self._draw_card_to_board(screen, card, slot_pos)

    def take_card(self, card) -> boolean:
        """
        Takes a card from the deck

        :pre: The card is on display
        :return: The card that was taken
        """
        for slot_pos, cardOnDisplay in self.cardsOnDisplay.items():
            if cardOnDisplay == card:
                self.cardsOnDisplay.pop(slot_pos)
                if not self.is_empty():
                    self.draw_card()
                return True
        return False

    def get_clicked_card(self, mouse_pos):
        """
        Returns the card that was clicked on, if any

        :param: mousePos: The position of the mouse click
        :return: The card that was clicked on, if any
        """
        # FIXME: improve algorithm by not checking all cards.
        for card in self.cardsOnDisplay.values():
            if card.is_clicked(mouse_pos):
                return card
        return None

    def _shuffle(self):
        """
        Shuffles the deck
        """
        shuffle(self.card_ids)

    def _fill_empty_slots(self):
        """
        Fills the empty slots in the deck with cards
        """
        while len(self.cardsOnDisplay) < self.card_slots:
            self.draw_card()

    def _add_card_to_display(self, card: Card):
        """
        Adds a card to the cards on display. The card is added to the first empty slot

        :param: card: The card to add to the display
        :pre: There is an empty slot in the deck
        """
        for i in range(1, self.card_slots + 1):
            if i not in self.cardsOnDisplay.keys():
                self.cardsOnDisplay[i] = card
                return

    def _display_deck_cover(self, screen):
        """
        Displays the deck cover on the board surface
        """
        width, height = Deck.get_deck_display_size(self.board)
        x = self._get_deck_x()
        if self.color == Color.RED:
            x -= width
        y = self._get_deck_y()
        image = pygame.transform.scale(self.image, (int(width), int(height)))
        screen.blit(image, (x, y))

    def _draw_card_to_board(self, screen, card, slot_pos):
        """
        Draws a card on the board surface

        :param: screen: The screen to draw the card on
        :param: card: The card to draw
        :param: slot_pos: The position of the card slot to draw the card to
        """
        card.draw(screen, *self._get_card_coords(slot_pos))

    def _get_card_coords(self, slot_pos):
        """
        Returns the position of the next card to be drawn
        """
        if self.color == Color.RED:
            x = self._get_deck_x() - slot_pos * self._x_distance_between_card_start_to_next_start() - \
                Card.get_card_size(self.board)[0]
        else:
            x = self._get_deck_x() + slot_pos * self._x_distance_between_card_start_to_next_start()
        y = self._get_deck_y()
        return (x, y)

    def _get_deck_pos(self):
        """
        Returns the position of the deck cover based on the size of the board
        """
        return (self._get_deck_x(), self._get_deck_y())

    def _get_deck_x(self):
        """
        Returns the x-coordinate of the side of the deck nearest to the board based on the size of the board
        """
        board = self.board
        if self.color == Color.RED:
            return board.get_x() + board.get_width() * (
                    1 - self.x_MarginToBoardWidthRatio)  # Red deck is on the right side of the board

        return board.get_x() + board.get_width() * self.x_MarginToBoardWidthRatio

    def _get_deck_y(self):
        """
        Returns the y-coordinate of the deck cover based on the size of the board
        """
        board = self.board
        distanceFromTop = (
                                  3 - self.level) * self._y_distance_between_card_start_to_next_start()  # based on the deck level
        return board.get_y() + (board.get_height() * self.y_MarginToBoardHeightRatio) + distanceFromTop

    def _get_image(self):
        """
        Returns the image of the deck cover
        """
        if hasattr(self, 'image'):
            return self.image
        if self.color == Color.RED:
            return pygame.image.load('sprites/cards/red{}/back.png'.format(str(self.level)))
        else:
            return pygame.image.load('sprites/cards/{}/back.png'.format(self.color.name.lower()))

    def _x_distance_between_cards(self):
        """
        Returns the distance between the end of the first card, and start of second card on the x-axis
        """
        return self.board.get_width() * self.x_DistanceBetweenCardsToBoardWidthRatio

    def _y_distance_between_cards(self):
        """
        Returns the distance between the end of the first card, and start of second card on the y-axis
        """
        return self.board.get_height() * self.y_DistanceBetweenCardsToBoardHeightRatio

    def _x_distance_between_card_start_to_next_start(self):
        """
        Returns the distance between the start of the first card, and start of second card on the x-axis
        """
        return self._x_distance_between_cards() + Card.get_card_size(self.board)[0]

    def _y_distance_between_card_start_to_next_start(self):
        """
        Returns the distance between the start of the first card, and start of second card on the y-axis
        """
        return self._y_distance_between_cards() + Card.get_card_size(self.board)[1]

    def _init_deck(self):
        for i in range(self._ID_START, self._ID_START + self._NUMBER_OF_CARDS):
            self.card_ids.append(i)

    def get_color(self):
        return self.color


@Singleton
class BlueDeck(Deck):
    _NUMBER_OF_CARDS = 20
    _ID_START = 71  # 71-90

    def __init__(self):
        super().__init__(level=3, color=Color.BLUE)


@Singleton
class GreenDeck(Deck):
    _NUMBER_OF_CARDS = 40
    _ID_START = 1  # 1- 40

    def __init__(self):
        super().__init__(level=1, color=Color.GREEN)


@Singleton
class YellowDeck(Deck):
    _NUMBER_OF_CARDS = 30
    _ID_START = 41  # 41-70

    def __init__(self):
        super().__init__(level=2, color=Color.YELLOW)


@Singleton
class RedDeck1(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 91

    def __init__(self):
        super().__init__(level=1, color=Color.RED, card_slots=2)


@Singleton
class RedDeck2(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 101

    def __init__(self):
        super().__init__(level=2, color=Color.RED, card_slots=2)


@Singleton
class RedDeck3(Deck):
    _NUMBER_OF_CARDS = 10
    _ID_START = 111

    def __init__(self):
        super().__init__(level=3, color=Color.RED, card_slots=2)
