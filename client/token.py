from board import Board
from flyweight import Flyweight
from color import Color
import pygame

@Flyweight
class Token:
    positions = {
        Color.WHITE: 0,
        Color.BLUE: 1,
        Color.GREEN: 2,
        Color.RED: 3,
        Color.BROWN: 4,
        Color.GOLD: 5,
    }

    multiplicities = {
        Color.WHITE: 7,
        Color.BLUE: 7,
        Color.GREEN: 7,
        Color.RED: 7,
        Color.BROWN: 7,
        Color.GOLD: 5,
    }

    yMargin = 31/33
    xMargin = 1/10
    xRatio =  1/15
    yRatio = 2/33
    xSeperationRatio = 1/20

    def __init__(self, color: Color, id: int):
        self._color = color
        self.image = pygame.image.load('sprites/tokens/{}.png'.format(color.name.lower()))
        self._id = id # Separates tokens with same color
        self.isOnDisplay = True
        self.pos = self._defaultPosition()

    @staticmethod
    def xStart():
        board = Board.instance()
        return  board.getX() + board.getWidth() * Token.xMargin

    @staticmethod
    def yStart():
        board = Board.instance()
        return board.getY() + board.getHeight() * Token.yMargin

    @staticmethod
    def initialize():
        id = 1
        for color in Token.positions.keys(): # for token color
            for _ in range(Token.multiplicities[color]): # for number of tokens
                Token.instance(color = color, id = id)
                id += 1

    @staticmethod
    def isWithinRange(mousePos):
        """
        Returns true if the mouse is within the range of the token display
        """
        range = Token.tokensRange()
        return range[0][0] <= mousePos[0] <= range[1][0] and range[0][1] <= mousePos[1] <= range[1][1]

    @staticmethod
    def tokensRange():
        """
        Returns the range of tokens
        """
        xStart = Token.xStart()
        yStart = Token.yStart()
        xEnd = xStart + len(Token.positions) * Token.distanceBetweenTokens()
        yEnd = yStart + Token.getSize()[1]
        return (xStart, yStart), (xEnd, yEnd)

    @staticmethod
    def distanceBetweenTokens():
        """
        Returns the distance between the start of one token and the start of the next token
        """
        board = Board.instance()
        width, height = Token.getSize()
        return width + board.getWidth() * Token.xSeperationRatio

    @staticmethod
    def displayAll(screen):
        for token in Token._flyweights.values():
            if token.isOnDisplay: # can be improved by not drawing same color tokens on top of each other
                token.draw(screen, *token.pos)

    @staticmethod
    def getClickedToken(mousePos):
        if not Token.isWithinRange(mousePos):
            return None
        # FIXME: Only look at tokens that are on display, and one of each color
        for token in Token._flyweights.values():
            if token.isOnDisplay and token.isClicked(mousePos):
                return token

    def takeToken(self):
        """
        Takes a token from the display
        """
        print('Taking token')
        self.isOnDisplay = False
    
    def draw(self, screen, x, y):
        image = pygame.transform.scale(self.image, Token.getSize())
        screen.blit(image, (x, y))

    @staticmethod
    def getSize():
        board = Board.instance()
        width = board.getWidth() * Token.xRatio
        height = board.getHeight() * Token.yRatio
        return (width, height)

    def getColor(self):
        return self._color
    
    def getRect(self):
        return self.image.get_rect()

    def isClicked(self, mousePos):
        """
        Returns true if the mouse is within the range of the token
        """
        x, y = self.pos
        width, height = Token.getSize()
        return x <= mousePos[0] <= x + width and y <= mousePos[1] <= y + height


    def _defaultPosition(self):
        x = self.xStart() + self.positions[self._color] * Token.distanceBetweenTokens()
        y = self.yStart()
        return (x, y)
