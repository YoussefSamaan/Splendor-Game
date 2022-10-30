
class Player:

  def __init__(self, name):
    self.turn = 0 # to check if it is the player's turn 
    self.name = name
    self.prestige_points = 0
    self.cards = [] # to store the bought cards
    self.red_discounts = 0
    self.blue_discounts = 0
    self.green_discounts = 0
    self.white_discounts = 0
    self.black_discounts = 0
    self.num_of_tokens = 0 # to check if they have over the limit and they need to return some
    self.reserved_cards = [] # to store the reserved cards
    self.gold_tokens = 0
    self.red_tokens = 0
    self.blue_tokens = 0
    self.green_tokens = 0
    self.white_tokens = 0
    self.black_tokens = 0
