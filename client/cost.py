class Cost:
    def __init__(self, red: int, green: int, blue: int, white: int, black: int):
        self._red = red
        self._green = green
        self._blue = blue
        self._white = white
        self._black = black

    def getRed(self):
        return self._red

    def getGreen(self):
        return self._green

    def getBlue(self):
        return self._blue

    def getWhite(self):
        return self._white

    def getBlack(self):
        return self._black

    def __eq__(self, other):
        return self._red == other._red and self._green == other._green and self._blue == other._blue and self._white == other._white and self._black == other._black

    def __str__(self):
        return "Red: " + str(self._red) + " Green: " + str(self._green) + " Blue: " + str(self._blue) + " White: " + str(self._white) + " Black: " + str(self._black)

    def __repr__(self):
        return self.__str__()