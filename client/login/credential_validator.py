import requests


def validate_credentials(username, password):
    url = 'http://localhost:8000/account/login'
    data = {'username': username, 'password': password}
    response = requests.post(url, data=data)
    return response.status_code == 200
