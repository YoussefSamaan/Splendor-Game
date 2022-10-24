from distutils.dist import Distribution
import sys
from time import sleep
import pygame, sys
from pygame.locals import *
from win32api import GetSystemMetrics
import os
from board import Board
from cost import Cost
from noble import Noble
from deck import BlueDeck, GreenDeck, YellowDeck

os.chdir(os.path.dirname(os.path.abspath(__file__))) # to make image imports start from current directory
WIDTH, HEIGHT = GetSystemMetrics(0), GetSystemMetrics(1)
pygame.init()
DISPLAYSURF = pygame.display.set_mode((0, 0), pygame.RESIZABLE)
pygame.display.set_caption('Splendor')
fullScreen = True

def initialize_game():
    initialize_board()
    initialize_cards()
    initialize_tokens()
    initialize_nobles()
    
def initialize_board():
    board = Board.instance(WIDTH, HEIGHT)
    board.draw(DISPLAYSURF)

def initialize_cards():
    BlueDeck.instance().draw(DISPLAYSURF)
    YellowDeck.instance().draw(DISPLAYSURF)
    GreenDeck.instance().draw(DISPLAYSURF)

def initialize_tokens():
    pass

def initialize_nobles():
    Noble.getClass().initialize(n = 4, screen = DISPLAYSURF)

def main():
    initialize_game()
    while True:
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    pygame.quit()
                    sys.exit()
        pygame.display.update()

if __name__ == '__main__':
    main()