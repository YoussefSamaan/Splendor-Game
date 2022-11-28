from authenticator import Authenticator
from game import splendor
from login import login

if __name__ == '__main__':
    authenticator = Authenticator()
    login.login(authenticator)
    splendor.play(authenticator=authenticator, game_id="8249419008812478999")
