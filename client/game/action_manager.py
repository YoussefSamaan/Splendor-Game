from game import server_manager
from game.action import Action
from game.card import Card
from game.splendorToken import Token
from typing import Dict


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

    def get_card_action_id(self, card: Card, player_name: str, action_type: Action) -> int:
        

        if action_type == Action.CANCEL:
            return 0
        if player_name != self.last_updated_player:
            # Not player's turn
            return -1
        for action in self.actions:
            if "card" in action and action["card"]["cardId"] == card.get_id()\
                and action["actionType"] == action_type.value:
                return action["actionId"]
        return -1

    # Given a Dict[Token,int], return the action id for taking tokens / returning tokens
    def get_token_action_id(self, token_selection: Dict[Token,int], player_name: str, action_type: Action) -> int:
        print(self.actions)
        
        if action_type == Action.CANCEL:
            return 0
        if player_name != self.last_updated_player:
            # Not player's turn
            return -1
        
        # Craft a new Dict[str,int] without non-zeroes
        color_key_dict = {}
        for token in token_selection:
            if token_selection[token] != 0:
                color_key_dict[token.get_color().name] = token_selection[token]

        # Find the action in the action list
        for action in self.actions:
            if "tokens" in action and action["tokens"] == color_key_dict\
                and action["actionType"] == action_type.value:
                return action["actionId"]
        return -2
    
    def get_actions_json(self):
        print(self.actions)

    def perform_action(self, action_id: int):
        server_manager.perform_action(self.authenticator, self.game_id, self.last_updated_player,
                                      action_id)


