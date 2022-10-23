import pygame
import sys

from singleton import Singleton

@Singleton
class Board:
    def __init__(self, screenWidth, screenHeight):
        self.boardImage = pygame.image.load('images/board.png')
        self.width = min(screenWidth, 600)
        self.height = min(screenHeight, 600)
        self.boardImage = pygame.transform.scale(self.boardImage, (self.width, self.height))
        self.boardRect = self.boardImage.get_rect()
        self.boardRect.center = (screenWidth / 2, screenHeight / 2)

    def draw(self, screen):
        screen.blit(self.boardImage, self.boardRect)
    
    def getRect(self):
        return self.boardRect
    
    def getWidth(self):
        return self.width

    def getHeight(self):
        return self.height
    
    def getCenter(self):
        return self.boardRect.center
    
    def isClicked(self, mousePos):
        return self.boardRect.collidepoint(mousePos)
    
    def initCards(self, cards):
        self.cards = cards
        self.cardRects = []
        for card in self.cards:
            self.cardRects.append(card.getRect())

    def initTokens(self, tokens):
        self.tokens = tokens
        self.tokenRects = []
        for token in self.tokens:
            self.tokenRects.append(token.getRect())
    
    def initNobles(self, nobles):
        self.nobles = nobles
        self.nobleRects = []
        for noble in self.nobles:
            self.nobleRects.append(noble.getRect())

    def initPlayers(self, players):
        self.players = players
        self.playerRects = []
        for player in self.players:
            self.playerRects.append(player.getRect())

    def initReserves(self, reserves):
        self.reserves = reserves
        self.reserveRects = []
        for reserve in self.reserves:
            self.reserveRects.append(reserve.getRect())
