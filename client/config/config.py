import hashlib

LOBBY_SERVICE_URL = 'http://localhost:4242'
SERVER_URL = 'http://10.122.101.159:8000'

def get_hash(value):
    return hashlib.md5(value.encode()).hexdigest()

def get_url():
    return LOBBY_SERVICE_URL