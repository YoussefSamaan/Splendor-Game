import hashlib

LOBBY_SERVICE_URL = 'http://localhost:4242'
SERVER_URL = 'http://192.168.1.7:8000'


def get_hash(value):
    return hashlib.md5(value.encode()).hexdigest()

def get_url():
    return LOBBY_SERVICE_URL