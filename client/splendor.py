import os
import sys

import pygame
from pygame.locals import *
from win32api import GetSystemMetrics
import os
from utils import *
from board import Board
from noble import Noble
from deck import *
from sidebar import *
from splendorToken import Token
from action import Action

os.chdir(os.path.dirname(os.path.abspath(__file__)))  # to make image imports start from current directory
WIDTH, HEIGHT = GetSystemMetrics(0), GetSystemMetrics(1)
FPS = 60
FPSCLOCK = pygame.time.Clock()
pygame.init()
DISPLAYSURF = pygame.display.set_mode((WIDTH, HEIGHT), pygame.FULLSCREEN)
pygame.display.set_caption('Splendor')
fullScreen = True
DECKS = [BlueDeck, RedDeck3, YellowDeck, RedDeck2, GreenDeck, RedDeck1]
FLASH_MESSAGE = None
FLASH_TIMER = 0


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


def show_flash_message():
    global FLASH_TIMER, FLASH_MESSAGE
    if FLASH_MESSAGE is None or FLASH_TIMER <= 0:
        return
    FLASH_TIMER -= 1
    flash_message(DISPLAYSURF, FLASH_MESSAGE, opacity=min(255, FLASH_TIMER * 2))


def set_flash_message(text, timer=60):
    global FLASH_MESSAGE, FLASH_TIMER
    FLASH_MESSAGE, FLASH_TIMER = text, timer

def display():
    # reset the display and re-display everything
    DISPLAYSURF.fill((0, 0, 0))
    display_sidebar()
    display_board()
    display_decks()
    display_tokens()
    display_nobles()

    show_flash_message()  # last so it's on top
    pygame.display.update()


def display_board():
    Board.instance().display(DISPLAYSURF)

def display_sidebar():
    # 0 = card, 1 = noble, 2 = reserve
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


def get_user_card_selection(card):
    """
    Get the user's selection of cards to reserve or buy
    :param card:
    :return:
    """
    dim_screen(DISPLAYSURF)
    action = card.get_user_selection(DISPLAYSURF)
    global FLASH_MESSAGE, FLASH_TIMER
    if action == Action.RESERVE:
        card.reserve()
        set_flash_message('Reserved a card')
    elif action == Action.BUY:
        card.buy()
        set_flash_message('Bought a card')
    elif action == Action.CANCEL:
        return
    else:
        raise ValueError('Invalid action')


def perform_action(obj):
    if obj is None:
        return
    if isinstance(obj, Card):
        get_user_card_selection(obj)
    elif isinstance(obj, Token):
        obj.take_token()
        set_flash_message('Took a token')
    elif isinstance(obj, Noble):
        obj.take_noble(Sidebar.instance())
        set_flash_message('Took a noble')

def check_toggle(mouse_pos):
    sidebar = Sidebar.instance()
    page_num = sidebar.is_clicked_toggle(mouse_pos)
    sidebar.toggle(page_num)


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
                if event.key == K_m:
                    # minimize the window
                    # FIXME: Is there a better way to do this?
                    pygame.display.set_mode((1, 1))
                if event.key == K_f:
                    pygame.display.set_mode((WIDTH, HEIGHT), pygame.FULLSCREEN)
                if event.key == pygame.K_UP :
                    Sidebar.instance().scroll_sidebar(-50)
                elif event.key == pygame.K_DOWN:
                    Sidebar.instance().scroll_sidebar(50)
            elif event.type == MOUSEBUTTONDOWN:
                if event.button == 4:
                    Sidebar.instance().scroll_sidebar(50)
                elif event.button == 5:
                    Sidebar.instance().scroll_sidebar(-50)
                else:
                    # check if it's the sidebar toggle
                    position = pygame.mouse.get_pos()
                    check_toggle(position)
                    obj = get_clicked_object(position)
                    perform_action(obj)

                    display()

        pygame.display.update()
        FPSCLOCK.tick(FPS)


if __name__ == '__main__':
    main()
