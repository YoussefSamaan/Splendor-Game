import requests
from config import config


def validate_credentials(username, password):
    url = config.lobby_service_url + '/oauth/token'
    data = {
        'grant_type': 'password',
        'username': username,
        'password': password
    }
    response = requests.post(url, data=data, auth=('bgp-client-name', 'bgp-client-pw'))
    return response.status_code == 200
