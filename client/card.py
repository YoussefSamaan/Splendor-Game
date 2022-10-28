from board import Board
from flyweight import Flyweight
import pygame
from color import Color


@Flyweight
class Card:
    x_ratio = 0.09  # ratio of card width to board width
    y_ratio = 0.12  # ratio of card height to board height

    def __init__(self, id: int, deck):
        self._id = id
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
                    return pygame.image.load('sprites/cards/red{}/{}.png'.format(i, self._id))
                except:
                    pass

        return pygame.image.load('sprites/cards/{}/{}.png'.format(self._color.name.lower(), self._id))
