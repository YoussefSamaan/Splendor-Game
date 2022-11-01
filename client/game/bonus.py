class Bonus:
    def __init__(self, red: int, green: int, blue: int, white: int, black: int):
        self._red = red
        self._green = green
        self._blue = blue
        self._white = white
        self._black = black

    def get_red(self):
        return self._red

    def get_green(self):
        return self._green

    def get_blue(self):
        return self._blue

    def get_white(self):
        return self._white

    def get_black(self):
        return self._black

    def __add__(self, other):
        self._red += other.get_red()
        self._green += other.get_green()
        self._blue += other.get_blue()
        self._white += other.get_white()
        self._black += other.get_black()
        return self

    def __sub__(self, other):
        self._red -= other.get_red()
        self._green -= other.get_green()
        self._blue -= other.get_blue()
        self._white -= other.get_white()
        self._black -= other.get_black()
        return self