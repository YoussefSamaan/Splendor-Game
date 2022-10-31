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
        self.cards_list = [] # to store the bought cards
        self.nobles_list = [] # to store the reserved nobles
        self.reserved_cards_list = [] # to store the reserved cards

        self.blue_discounts = 0
        self.green_discounts = 0
        self.red_discounts = 0
        self.brown_discounts = 0
        self.white_discounts = 0
        self.num_of_tokens = 0 # to check if they have over the limit and they need to return some
        
        self.blue_tokens = 0
        self.green_tokens = 0
        self.red_tokens = 0
        self.brown_tokens = 0
        self.white_tokens = 0
        self.gold_tokens = 0

    def take_token(self, color):
        # Overloaded take_token function for 2 tokens of the same type (color)
        self.add_token(self, color, 2) #adds the token and updates num_of_tokens

    def take_token(self, color1, color2, color3):
        # Overloaded take_token function for 3 tokens of different types
        # usage example: take_token(self, Color.BLUE, Color.GREEN, Color.RED)
        self.add_token(self, color1, 1)
        self.add_token(self, color2, 1)
        self.add_token(self, color3, 1)
    
    
    def add_token(self, color, number_of_tokens):
        # This function adds the token to the player and updates num_of_tokens
        # usage example: add_token(self, Color.GOLD, 1)
        if color == Color.BLUE:
            self.blue_tokens += number_of_tokens
        elif color == Color.GREEN:
            self.green_tokens += number_of_tokens
        elif color == Color.RED:  
            self.red_tokens += number_of_tokens
        elif color == Color.BROWN:
            self.brown_tokens += number_of_tokens
        elif color == Color.WHITE:  
            self.white_tokens += number_of_tokens
        elif color == Color.GOLD:
            self.gold_tokens += number_of_tokens
        else:
            return 
        # only update number of tokens if it's a valid colour
        self.num_of_tokens += number_of_tokens

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
        self.cards_list.append(card)
        self.add_bonus(self, card)
        self.add_prestige(self, card)

    def add_bonus(self, card):
        # add the discounts from a card
        self.red_discounts += card.bonus.get_red(card.bonus)
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
        self.reserved_cards_list.append(card)
        self.add_token(self, Color.GOLD, 1)

    def reserve_noble(self, noble_card):
        # reserves a noble. 
        # TODO: Figure out noble card. Current noble class does not have prestige attribute.
        self.nobles_list.append(noble_card)
        self.add_prestige(self, noble_card)