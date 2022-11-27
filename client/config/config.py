import hashlib

LOBBY_SERVICE_URL = 'http://localhost:4242'
SERVER_URL = 'http://localhost:8000'


def get_hash(value):
    return hashlib.md5(value.encode()).hexdigest()
