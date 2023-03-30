import hashlib

LOBBY_SERVICE_URL = 'http://localhost:4242'
SERVER_URL = 'http://localhost:8000' # replace this line with your local ip


def get_hash(value):
    return hashlib.md5(value.encode()).hexdigest()

def get_url():
    return LOBBY_SERVICE_URL