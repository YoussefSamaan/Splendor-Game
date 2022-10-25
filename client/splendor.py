import pygame, sys
from pygame.locals import *
from win32api import GetSystemMetrics
import os
from board import Board
from token import Token
from noble import Noble
from deck import *

os.chdir(os.path.dirname(os.path.abspath(__file__))) # to make image imports start from current directory
WIDTH, HEIGHT = GetSystemMetrics(0), GetSystemMetrics(1)
FPS = 60
FPSCLOCK = pygame.time.Clock()
pygame.init()
DISPLAYSURF = pygame.display.set_mode((0,0), pygame.RESIZABLE)
pygame.display.set_caption('Splendor')
fullScreen = True
DECKS = [BlueDeck, RedDeck3, YellowDeck, RedDeck2, GreenDeck, RedDeck1]

def initialize_game():
    initialize_board()
    initialize_cards()
    initialize_tokens()
    initialize_nobles()

def initialize_board():
    Board.instance(WIDTH, HEIGHT)

def initialize_cards():
    BlueDeck.instance()
    RedDeck3.instance()
    YellowDeck.instance()
    RedDeck2.instance()
    GreenDeck.instance()
    RedDeck1.instance()

def initialize_tokens():
    Token.initialize()

def initialize_nobles():
    Noble.initialize(n = 4)
        
def display():
    # reset the display and re-display everything
    DISPLAYSURF.fill((0,0,0))
    displayBoard()
    displayDecks()
    displayTokens()
    displayNobles()
    pygame.display.update()

def displayBoard():
    Board.instance().display(DISPLAYSURF)

def displayDecks():
    BlueDeck.instance().display(DISPLAYSURF)
    RedDeck3.instance().display(DISPLAYSURF)
    YellowDeck.instance().display(DISPLAYSURF)
    RedDeck2.instance().display(DISPLAYSURF)
    GreenDeck.instance().display(DISPLAYSURF)
    RedDeck1.instance().display(DISPLAYSURF)

def displayTokens():
    Token.displayAll(DISPLAYSURF)

def displayNobles():
    Noble.displayAll(DISPLAYSURF)

def getClickedObject(pos):
    board = Board.instance()
    if (not board.isClicked(pos)):
        return None
    for deck in DECKS:
        card = deck.instance().getClickedCard(pos)
        if card is not None:
            return card
    token = Token.getClickedToken(pos)
    if token is not None:
        return token
    noble = Noble.getClickedNoble(pos)
    if noble is not None:
        return noble
    return None

def performAction(obj):
    if obj is None:
        return
    if isinstance(obj, Card):
        deck = obj.getDeck()
        deck.takeCard(obj)
    elif isinstance(obj, Token):
        obj.takeToken()
    elif isinstance(obj, Noble):
        obj.takeNoble()

def main():
    initialize_game()
    display()
    while True:
        display()
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    pygame.quit()
                    sys.exit()
            elif event.type == MOUSEBUTTONDOWN:
                obj = getClickedObject(pygame.mouse.get_pos())
                print("Clicked on", obj)
                performAction(obj)
                display()
            
        pygame.display.update()
        FPSCLOCK.tick(FPS)

if __name__ == '__main__':
    main()
