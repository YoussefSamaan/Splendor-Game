import os
import sys

import pygame
from pygame.locals import *
from win32api import GetSystemMetrics
import os
from board import Board
from noble import Noble
from deck import *
from sidebar import Sidebar
from splendorToken import Token

os.chdir(os.path.dirname(os.path.abspath(__file__)))  # to make image imports start from current directory
WIDTH, HEIGHT = GetSystemMetrics(0), GetSystemMetrics(1)
FPS = 60
FPSCLOCK = pygame.time.Clock()
pygame.init()
DISPLAYSURF = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)
pygame.display.set_caption('Splendor')
fullScreen = True
DECKS = [BlueDeck, RedDeck3, YellowDeck, RedDeck2, GreenDeck, RedDeck1]


def initialize_game():
    initialize_board()
    initialize_sidebar()
    initialize_cards()
    initialize_tokens()
    initialize_nobles()


def initialize_board():
    Board.instance(WIDTH, HEIGHT)

def initialize_sidebar():
    Sidebar.instance(WIDTH, HEIGHT)

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
    Noble.initialize(n=4)


def display():
    # reset the display and re-display everything
    DISPLAYSURF.fill((0, 0, 0))
    display_board()
    display_decks()
    display_tokens()
    display_nobles()
    display_sidebar()
    pygame.display.update()


def display_board():
    Board.instance().display(DISPLAYSURF)

def display_sidebar():
    Sidebar.instance().display(DISPLAYSURF)


def display_decks():
    BlueDeck.instance().display(DISPLAYSURF)
    RedDeck3.instance().display(DISPLAYSURF)
    YellowDeck.instance().display(DISPLAYSURF)
    RedDeck2.instance().display(DISPLAYSURF)
    GreenDeck.instance().display(DISPLAYSURF)
    RedDeck1.instance().display(DISPLAYSURF)


def display_tokens():
    Token.display_all(DISPLAYSURF)


def display_nobles():
    Noble.display_all(DISPLAYSURF)


def get_clicked_object(pos):
    board = Board.instance()
    if not board.is_clicked(pos):
        return None
    for deck in DECKS:
        card = deck.instance().get_clicked_card(pos)
        if card is not None:
            return card
    token = Token.get_clicked_token(pos)
    if token is not None:
        return token
    noble = Noble.get_clicked_noble(pos)
    if noble is not None:
        return noble
    return None


def perform_action(obj):
    if obj is None:
        return
    if isinstance(obj, Card):
        deck = obj.get_deck()
        deck.take_card(obj)
    elif isinstance(obj, Token):
        obj.take_token()
    elif isinstance(obj, Noble):
        obj.take_noble(Sidebar.instance())
    elif isinstance(obj, Sidebar):
        obj.scroll_sidebar()


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
                obj = get_clicked_object(pygame.mouse.get_pos())
                perform_action(obj)
                display()
                # add schoolable sidebar

        pygame.display.update()
        FPSCLOCK.tick(FPS)


if __name__ == '__main__':
    main()
