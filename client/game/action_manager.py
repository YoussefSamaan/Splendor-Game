from client.game import server_manager
from client.game.action import Action
from client.game.card import Card


class ActionManager:
    def __init__(self, game_id, authenticator):
        self.game_id = game_id
        self.authenticator = authenticator
        self.actions = []
        self.last_updated_player = None

    def update(self, player_name):
        if self.last_updated_player == player_name and self.last_updated_player is not None:
            # To avoid unnecessary requests
            return
        self.last_updated_player = player_name
        self.actions = server_manager.get_actions(self.authenticator, self.game_id, player_name)

    def is_valid_card_action(self, card: Card, player_name: str, action_type: Action):
        print(card, action_type.value)
        if action_type == Action.CANCEL:
            return True
        if player_name != self.last_updated_player:
            # Not player's turn
            return False
        for action in self.actions:
            print(action)
            if action['cardType'] != 'DevelopmentCard' or action['actionType'] != action_type.value\
                    or action['actionId'] != card.get_id():
                continue
            return True
        return False


