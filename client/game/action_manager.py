from game import server_manager
from game.action import Action
from game.card import Card


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

    def get_card_action_id(self, card: Card, player_name: str, action_type: Action):
        if action_type == Action.CANCEL:
            return 0
        if player_name != self.last_updated_player:
            # Not player's turn
            return -1
        for action in self.actions:
            if action['cardType'] != 'DevelopmentCard' or action['actionType'] != action_type.value\
                    or action['card']['cardId'] != card.get_id():
                continue
            return action['actionId']
        return -1

    def perform_action(self, action_id: int):
        server_manager.perform_action(self.authenticator, self.game_id, self.last_updated_player,
                                      action_id)


