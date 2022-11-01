from turtle import color
from card import Card
from noble import Noble
from color import Color

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

    def __init__(self, name):
        self._max_number_of_tokens = 10
        self.turn = 0 # to check if it is the player's turn 
        self.name = name
        self.prestige_points = 0
        self.cards_bought = {} # to store the bought cards
        self.nobles = {} # to store the reserved nobles
        self.reserved_cards = {} # to store the reserved cards

# for sidebar
        self.last_position_card = (0,self.card_size[1] / 4 + 10)
        self.last_position_noble = (0,self.card_size[1] / 4 + 10)
        self.last_position_reserved = (0,self.card_size[1] / 4 + 10)
        self.discounts = {
            Color.BLUE: 0,
            Color.BROWN: 0,
            Color.GREEN: 0,
            Color.RED: 0,
            Color.WHITE: 0
        }

        self.tokens = {
            Color.BLUE: 0,
            Color.BROWN: 0,
            Color.GREEN: 0,
            Color.RED: 0,
            Color.WHITE: 0,
            Color.GOLD: 0
        }

    def add_noble_to_sidebar(self, noble):
        self.nobles[noble] = self.last_position_noble
        self.last_position_noble = (self.last_position_noble[0], self.last_position_noble[1]+self.noble_size[1])

    def add_card_to_sidebar(self, card):
        self.cards[card] = self.last_position_card
        self.last_position_card = (self.last_position_card[0], self.last_position_card[1]+self.card_size[1])

    def reserve_card_to_sidebar(self, reserved):
        self.reserved_cards[reserved] = self.last_position_reserved
        self.last_position_reserved = (self.last_position_reserved[0], self.last_position_reserved[1]+self.card_size[1])
     
    def take_token(self, color):
        # Overloaded take_token function for 2 tokens of the same type (color)
        self.tokens[color] += 2  #adds the token and updates num_of_tokens

    def take_token(self, color1, color2, color3):
        # Overloaded take_token function for 3 tokens of different types
        # usage example: take_token(self, Color.BLUE, Color.GREEN, Color.RED)
        self.tokens[color1] += 1
        self.tokens[color2] += 1
        self.tokens[color3] += 1
    
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
        #self.cards_list.append(card)
        self.add_bonus(self, card)
        self.add_prestige(self, card)
        self.add_card_to_sidebar

    def add_bonus(self, card):
        # add the discounts from a card
        self.discounts[Color.RED] += card.bonus.get_red(card.bonus)
        self.green_discounts += card.bonus.get_green(card.bonus)
        self.blue_discounts += card.bonus.get_blue(card.bonus)
        self.white_discounts += card.bonus.get_white(card.bonus)
        self.brown_discounts += card.bonus.get_brown(card.bonus)
    
    def remove_bonus(self, card):
        # remove the discounts from a card
        self.red_discounts -= card.bonus.get_red(card.bonus)
        self.green_discounts -= card.bonus.get_green(card.bonus)
        self.blue_discounts -= card.bonus.get_blue(card.bonus)
        self.white_discounts -= card.bonus.get_white(card.bonus)
        self.brown_discounts -= card.bonus.get_brown(card.bonus)

    def add_prestige(self, card):
        self.prestige_points += card.get_prestige_points()

    def remove_prestige(self, card):
        self.prestige_points -= card.get_prestige_points()
    
    def reserve_card(self, card):
        # TODO: rename this to avoid confusion. This function is for adding a card to the player's reserved_cards_list.
        #self.reserved_cards_list.append(card)
        self.add_token(self, Color.GOLD, 1)
        self.reserve_card_to_sidebar

    def reserve_noble(self, noble_card):
        # reserves a noble. 
        # TODO: Figure out noble card. Current noble class does not have prestige attribute.
        #self.nobles_list.append(noble_card)
        self.add_prestige(self, noble_card)
        self.add_noble_to_sidebar