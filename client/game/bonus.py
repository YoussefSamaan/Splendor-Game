import pygame

from color import Color


class Bonus:
    def __init__(self, red: int, green: int, blue: int, white: int, brown: int):
        self.bonus = {
            Color.RED: red,
            Color.GREEN: green,
            Color.BLUE: blue,
            Color.WHITE: white,
            Color.BROWN: brown
        }

    def get_red(self):
        return self.bonus[Color.RED]

    def get_green(self):
        return self.bonus[Color.GREEN]

    def get_blue(self):
        return self.bonus[Color.BLUE]

    def get_white(self):
        return self.bonus[Color.WHITE]

    def get_black(self):
        return self.bonus[Color.BROWN]

    def get_all(self):
        return self.bonus

    def draw(self, surface):
        """
        Draws the bonus on the surface. Bonuses are evenly spaced out to fill the surface
        :param surface: The surface to draw on
        :return: None
        """
        width = surface.get_width() / len(self.bonus)
        x = 0
        for color in self.bonus.keys():
            pygame.draw.rect(surface, color.value, (x, 0, width, surface.get_height()))
            x += width

    def __add__(self, other):
        for color in self.bonus.keys():
            self.bonus[color] += other.bonus[color]
        return self

    def __sub__(self, other):
        for color in self.bonus.keys():
            self.bonus[color] -= other.bonus[color]
        return self
