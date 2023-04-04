from authenticator import Authenticator
from game import splendor
from login import login
from session import session

if __name__ == '__main__':
    authenticator = Authenticator()
    login.login(authenticator)
    while True:
      game_id = session.session(authenticator)
      splendor.play(authenticator=authenticator, game_id=game_id)
