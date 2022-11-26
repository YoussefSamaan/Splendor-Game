import requests
from authenticator import Authenticator
from config import config


def get_board(authenticator: Authenticator, game_id: str):
    """
    Returns the board of the game with the given id.
    :param authenticator: The authenticator of the user.
    :param game_id: The id of the game.
    :return: The board of the game.
    """
    url = config.server_url + '/api/games/' + game_id + '/board'
    data = {
        "username": authenticator.username,
        "access_token": authenticator.get_token()
    }
    response = requests.get(url, data=data)
    if response.status_code == 200:
        return response.json()
    raise Exception("Could not get board")


def get_actions(authenticator: Authenticator, game_id: str, player_name: str):
    """
    Returns the actions of the game with the given id.
    :param player_name: the name of the player performing the action
    :param authenticator: The authenticator of the user.
    :param game_id: The id of the game.
    :return: The actions of the game.
    """
    url = config.server_url + '/api/games/' + game_id + '/players/' + player_name + '/actions'
    data = {
        "username": authenticator.username,
        "access_token": authenticator.get_token()
    }
    response = requests.get(url, data=data)
    if response.status_code == 200:
        print(response.json())
        return response.json()
    raise Exception("Could not get actions")


