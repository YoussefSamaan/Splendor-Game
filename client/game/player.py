import pygame

import utils
from board import Board
from bonus import Bonus
from card import Card
from color import Color
from flyweight import Flyweight
from noble import Noble
from splendorToken import Token
from utils import write_on


@Flyweight
class Player:
    '''
    Player needs to:
    - take tokens
    - buy card (TODO)
    - choose tokens for payment (TODO)
    - choose cards for payment (TODO)
    - reserve card
    - reserve noble
    '''

    BACKGROUND_COLOR = utils.BACKGROUND_COLOR
    BORDER_COLOR = Color.BLACK.value
    HIGHLIGHT_COLOR = Color.YELLOW.value
    NAME_RATIO = 1 / 4
    TOKENS_RATIO = 3 / 8
    DISCOUNTS_RATIO = 3 / 8
    BORDER_SIZE = 2.5

    assert NAME_RATIO + TOKENS_RATIO + DISCOUNTS_RATIO == 1, "Ratios must add up to 1"

    def __init__(self, name, id):
        self._max_number_of_tokens = 10
        self.turn = 0  # to check if it is the player's turn
        self.name = name
        self.prestige_points = 0
        self.cards_bought = {}  # to store the bought cards
        self.nobles = {}  # to store the reserved nobles
        self.reserved_cards = {}  # to store the reserved cards
        self.pos = id

        # for sidebar
        self.last_position_card = (0, Card.get_card_size()[1] / 4 + 10)
        self.last_position_noble = (0, Card.get_card_size()[1] / 4 + 10)
        self.last_position_reserved = (0, Card.get_card_size()[1] / 4 + 10)
        self.discounts = Bonus(0, 0, 0, 0, 0)

        self.tokens = {
            Color.BLUE: 0,
            Color.BROWN: 0,
            Color.GREEN: 0,
            Color.RED: 0,
            Color.WHITE: 0,
            Color.GOLD: 0
        }

    def is_clicked(self, position, width, height, num):
        board = Board.instance()
        x_start = self.pos * width / num
        y_start = height - board.height_offset
        x_end = (self.pos + 1) * width / num
        y_end = height
        return x_start <= position[0] <= x_end and y_start <= position[1] <= y_end

    def add_noble_to_sidebar(self, noble):
        self.nobles[noble] = self.last_position_noble
        self.last_position_noble = (self.last_position_noble[0], self.last_position_noble[1] + Noble.get_card_size()[1])

    def add_card_to_sidebar(self, card):
        self.cards_bought[card] = self.last_position_card
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1] + Card.get_card_size()[1])

    def reserve_card_to_sidebar(self, reserved):
        self.reserved_cards[reserved] = self.last_position_reserved
        self.last_position_reserved = (
            self.last_position_reserved[0], self.last_position_reserved[1] + Card.get_card_size()[1])

    def add_token(self, token):
        self.tokens[token.get_color()] += 1

    def get_number_of_tokens(self):
        # TODO: fill in this function
        pass

    def return_coins(self):
        '''
        TODO:Fill this function
        '''
        pass

    def buy_card(self, card):
        '''
        TODO:
        Check discount for price.
        Allow player to choose which cards/tokens they want to pay with.
        Remove those tokens or cards (+ remove card bonuses: discount/prestige) from player.
        Call add_card.
            (Handled by add_card) Add card to player. 
            (Handled by add_card) Add card bonuses to player.
            (Handled by add_card) Add card prestige points to player.
        '''
        self.add_card(card)

    def add_card(self, card):
        # add a card and its bonuses/prestige points to a player
        """
        Add card to player. 
        Add card bonuses (discounts) to player.
        Add card prestige points to player.
        """
        # self.cards_list.append(card)
        self.add_bonus(card)
        self.add_prestige(card)
        self.add_card_to_sidebar(card)

    def add_bonus(self, card):
        # add the discounts from a card
        self.discounts + card.get_bonus()

    def remove_bonus(self, card):
        # remove the discounts from a card
        self.discounts - card.get_bonus()

    def add_prestige(self, card):
        self.prestige_points += card.get_prestige_points()

    def remove_prestige(self, card):
        self.prestige_points -= card.get_prestige_points()

    def reserve_card(self, card):
        # TODO: rename this to avoid confusion. This function is for adding a card to the player's reserved_cards_list.
        # self.reserved_cards_list.append(card)
        self.tokens[Color.GOLD] += 1
        self.reserve_card_to_sidebar(card)

    def reserve_noble(self, noble_card):
        # reserves a noble. 
        # TODO: Figure out noble card. Current noble class does not have prestige attribute.
        # self.nobles_list.append(noble_card)
        self.add_prestige(noble_card)
        self.add_noble_to_sidebar(noble_card)

    def show_name(self, inventory: pygame.Surface):
        surface = pygame.Surface((inventory.get_width(), inventory.get_height() * self.NAME_RATIO))
        surface.fill(self.BACKGROUND_COLOR)
        write_on(surface, self.name)
        inventory.blit(surface, (inventory.get_width() / 2 - surface.get_width() / 2, 0))

    def show_tokens(self, inventory: pygame.Surface):
        surface = pygame.Surface((inventory.get_width(), inventory.get_height() * self.TOKENS_RATIO))
        surface.fill(self.BACKGROUND_COLOR)
        token_size = (surface.get_width() / 6, surface.get_height())
        for i, color in enumerate(self.tokens):
            token = Token.get_token(color)
            token.draw(surface, x=token_size[0] * i, y=0, amount=self.tokens[color], size=token_size)
        inventory.blit(surface, (0, inventory.get_height() * self.NAME_RATIO))  # Should be below name

    def show_prestige_points(self, inventory: pygame.Surface):
        """
        Shows prestige points in top left corner of inventory
        :param inventory:
        :return:
        """
        surface = pygame.Surface((inventory.get_width() * self.NAME_RATIO, inventory.get_height() * self.NAME_RATIO))
        surface.fill(self.BACKGROUND_COLOR)
        write_on(surface, str(self.prestige_points), font_size=surface.get_height(), color=Color.BROWN.value)
        inventory.blit(surface, (0, 0))

    def show_discounts(self, inventory: pygame.Surface):
        surface = pygame.Surface((inventory.get_width(), inventory.get_height() * self.DISCOUNTS_RATIO))
        surface.fill(self.BACKGROUND_COLOR)
        self.discounts.draw(surface)
        inventory.blit(surface, (0, inventory.get_height() * (self.NAME_RATIO + self.TOKENS_RATIO)))

    def display(self, screen: pygame.Surface, num_players: int, highlighted: bool = False):
        """
        Draw the player's Inventory.
        :param highlighted: whether the player is the current player
        :param screen:
        :param num_players: number of players
        :return:
        """
        width = screen.get_width() // num_players
        height = screen.get_height() - Board.instance().get_height()
        x = self.pos * width
        y = Board.instance().get_height()

        # Draw the border
        inventory = pygame.Surface((width, height))
        if highlighted:
            inventory.fill(self.HIGHLIGHT_COLOR)
        else:
            inventory.fill(self.BORDER_COLOR)
        screen.blit(inventory, (x, y))

        # Draw the actual inventory
        inventory = pygame.Surface((width - 2 * self.BORDER_SIZE, height - 2 * self.BORDER_SIZE))
        inventory.fill(self.BACKGROUND_COLOR)

        self.show_name(inventory)
        self.show_tokens(inventory)
        self.show_prestige_points(inventory)
        self.show_discounts(inventory)

        screen.blit(inventory, (x + self.BORDER_SIZE, y + self.BORDER_SIZE))
