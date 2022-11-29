import os
import pygame
import sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
from authenticator import *

from session import get_session, post_session, delete_session, put_session


HEIGHT = 750
WIDTH = 900
GREY = (57, 57, 57)
WHITE = (255, 255, 255)
LIGHT_GREY = (99, 99, 99)
LIGHT_BLUE = "lightskyblue3"
GREEN = (0, 204, 0)
RED = (255, 0, 0)
FPS = 60

os.chdir(os.path.dirname(os.path.abspath(__file__)))
pygame.init()
pygame.display.set_caption('Splendor')
base_font = pygame.font.Font(None, 28)  # font, size
clock = pygame.time.Clock()
screen = pygame.display.set_mode((WIDTH, HEIGHT))  # , pygame.FULLSCREEN

splendor_text = pygame.image.load('../sprites/splendor-title.png')
splendor_text = pygame.transform.scale(splendor_text, (500, 200))

color_active = pygame.Color(LIGHT_BLUE)
color_passive = pygame.Color(LIGHT_GREY)
color_error = pygame.Color(RED)




'''ALL FUNCTIONS HERE HAVE TO BE CHANGED'''
def get_games(sessions):
    # gets games currently stored in memory
    names = []
    for game in sessions:
        names.append(sessions[game]["savegameid"])
    return names
    
def get_joined_games(sessions,current_player):
    # get every game joined by the curr player
    games = []
    for game in sessions:
        if current_player.username in sessions[game]['players']:
            games.append(game)
    return games

def get_players(sessions):
    # gets players currently stored in memory
    players = []
    for game in sessions:
        p = list(sessions[game]['players'])
        p.pop(0) # don't include creator in list of players to reduce clutter
        p_clean = ', '.join(p) #remove brackets from e.g. ['maex', 'linus'] to get maex, linus
        players.append(str(p_clean))
    return players

# This function gets the creators of all games. Is this intended?
def get_creators(sessions):
    # gets creator of the game
    creators = []
    for game in sessions:
        creator = sessions[game]['creator']
        creators.append(str(creator))
    return creators

def is_game_launched(sessions,game_name):
    for game in sessions:
        if sessions[game]["savegameid"] == game_name:
            return sessions[game]["launched"]
    return False


def session(authenticator):
    # needs to add game to the list of games
    # some sort of scrolling game inventory

    create_input_rect = pygame.Rect((150, 250, 200, 35))  # pos_x, pos_y, width, height

    create_rect = pygame.Rect((350, 300, 200, 70))
    join_rect = pygame.Rect((350, 600, 200, 70))
    back_rect = pygame.Rect((50, 100, 150, 70))
    previous_button_rect = pygame.Rect((150, 660, 150, 70))
    next_button_rect = pygame.Rect((600, 660, 150, 70))
    delete_text = base_font.render("Delete", True, WHITE)
    base_text = base_font.render("Create a new game", True, WHITE)
    back_text = base_font.render('Log Out', True, WHITE)
    back_text2 = base_font.render('Back', True, WHITE)
    play_text = base_font.render('Play', True, WHITE)
    create_text = base_font.render('Create', True, WHITE)
    join_text = base_font.render('Join', True, WHITE)
    launch_text = base_font.render('Launch', True, WHITE)
    leave_text = base_font.render('Leave', True, WHITE)
    next_text = base_font.render('Next', True, WHITE)
    previous_text = base_font.render('Previous', True, WHITE)
    create_text_display = base_font.render('Session Name', True, WHITE)
    create_color = color_passive
    leave_rect = pygame.Rect((655, 450, 100, 55)) # creator can't leave game 
    create_text_entry = ""

    game_rect1 = pygame.Rect((150, 450, 400, 55))
    game_rect2 = pygame.Rect((150, 550, 400, 55))
    del_rect1 = pygame.Rect((555, 450, 90, 55))
    del_rect2 = pygame.Rect((555, 550, 90, 55))
    launch_rect1 = pygame.Rect((655, 450, 90, 55))
    launch_rect2 = pygame.Rect((655, 550, 90, 55))

    play_rect1 = pygame.Rect((555, 450, 90, 55))
    play_rect2 = pygame.Rect((555, 550, 90, 55))
    current_page = 0
    wrong_credentials = False # like session somehow invalid
    create_active = False # whether you're clicked on the text input

    def create_game(game):
        return post_session.create_session(authenticator.username, authenticator.get_token(), game).text
    def join(game):
        while True:
            screen.fill(GREY)
            newtext = base_font.render("Joining " + game +" confirm?", True, WHITE)
 
            pygame.draw.rect(screen, RED, back_rect)

            pygame.draw.rect(screen, GREEN, join_rect)
            screen.blit(newtext, (350, 350))
            screen.blit(back_text2, (185, 125))
            screen.blit(join_text, (400, 625))
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:

                    if back_rect.collidepoint(event.pos):
                        screen.fill(GREY)
                        session(authenticator)

                    elif join_rect.collidepoint(event.pos):
                        put_session.add_player(authenticator.get_token(), game, authenticator.username)

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        pygame.quit()
                        sys.exit()
            pygame.display.flip()
            clock.tick(FPS)
    def leave(game):
        while True:
            screen.fill(GREY)
            newtext = base_font.render("Leaving " + game +" confirm?", True, WHITE)
 
            pygame.draw.rect(screen, RED, back_rect)

            pygame.draw.rect(screen, RED, leave_rect)
            screen.blit(newtext, (350, 350))
            screen.blit(back_text2, (185, 125))
            screen.blit(leave_text, (400, 625))
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:

                    if back_rect.collidepoint(event.pos):
                        screen.fill(GREY)
                        session(authenticator)

                    elif leave_rect.collidepoint(event.pos):
                        delete_session.remove_player(authenticator.get_token(), game, authenticator.username)

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        pygame.quit()
                        sys.exit()
            pygame.display.flip()
            clock.tick(FPS)
    def delete(game):
        delete_session.delete_session(authenticator.get_token(), game)
    
    while True:
        screen.fill(GREY)
        i = current_page * 2
        # if we are on i = 3, page =1 we only need to display 1 game
        # len getgames would be 3

        sessions_json = get_session.get_all_sessions().json()["sessions"]

        if len(get_games(sessions_json)) == 0:
            pass
        elif len(get_games(sessions_json))-i > 0:
            game_name = base_font.render(get_games(sessions_json)[i] + " / "+ get_creators(sessions_json)[i] + " / " + get_players(sessions_json)[i], True, WHITE)
            screen.blit(game_name, (game_rect1[0]+20, game_rect1[1]+20))
            pygame.draw.rect(screen, LIGHT_BLUE, game_rect1, 3)
            if get_creators(sessions_json)[i] == authenticator.username:
                pygame.draw.rect(screen, RED, del_rect1)
                screen.blit(delete_text, (del_rect1[0]+20, del_rect1[1]+20))
                if is_game_launched(sessions_json,get_games(sessions_json)[i]):
                    pygame.draw.rect(screen, GREEN, play_rect1)
                    screen.blit(play_text, (play_rect1[0]+20, play_rect1[1]+20))
                else:
                    pygame.draw.rect(screen, GREEN, launch_rect1)
                    screen.blit(launch_text, (launch_rect1[0]+20, launch_rect1[1]+20))
            elif get_games(sessions_json)[i] in get_joined_games(sessions_json,authenticator):
                    pygame.draw.rect(screen, GREEN, launch_rect1)
                    screen.blit(launch_text, (launch_rect1[0]+10, launch_rect1[1]+20))

            
        if len(get_games(sessions_json))-i > 1:
            game_name2 = base_font.render(get_games(sessions_json)[i+1] + " / " + get_creators(sessions_json)[i+1] + " / " + get_players(sessions_json)[i+1], True, WHITE)
            pygame.draw.rect(screen, LIGHT_BLUE, game_rect2, 3)
            screen.blit(game_name2, (game_rect2[0]+20, game_rect2[1]+20))
            if get_creators(sessions_json)[i+1] == authenticator.username:
                pygame.draw.rect(screen, RED, del_rect2)
                screen.blit(delete_text, (del_rect2[0]+20, del_rect2[1]+20))
                if is_game_launched(sessions_json,get_games(sessions_json)[i+1]):
                    pygame.draw.rect(screen, GREEN, play_rect2)
                    screen.blit(play_text, (play_rect2[0]+20, play_rect2[1]+20))
                else:
                    pygame.draw.rect(screen, GREEN, launch_rect2)
                    screen.blit(launch_text, (launch_rect2[0]+10, launch_rect2[1]+20))
            elif get_games(sessions_json)[i+1] in get_joined_games(sessions_json,authenticator):
                    pygame.draw.rect(screen, GREEN, launch_rect2)
                    screen.blit(launch_text, (launch_rect2[0]+10, launch_rect2[1]+20))

        # add code to make sure only creator can delete game 
        # add code to make sure only joined player can leave game 

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.MOUSEBUTTONDOWN:
                if game_rect1.collidepoint(event.pos):
<<<<<<< HEAD
                    if get_creators(sessions_json)[i] != authenticator.username:
                        if get_games(sessions_json)[i] in get_joined_games(sessions_json, authenticator):
                            leave(get_games(sessions_json)[i])
=======
                    if get_creators()[i] != authenticator.username:
                        if get_games()[i] in get_joined_games(authenticator):
                            leave(get_games()[i])
>>>>>>> 9c67d5ee18b8695ff268fffda57fc8bb8aa5a053
                        else:
                            join(get_games(sessions_json)[i])
                elif game_rect2.collidepoint(event.pos):
<<<<<<< HEAD
                    if get_creators(sessions_json)[i+1] != authenticator.username:
                        if get_games(sessions_json)[i+1] in get_joined_games(sessions_json, authenticator):
                            leave(get_games(sessions_json)[i+1])
=======
                    if get_creators()[i+1] != authenticator.username:
                        if get_games()[i+1] in get_joined_games(authenticator):
                            leave(get_games()[i+1])
>>>>>>> 9c67d5ee18b8695ff268fffda57fc8bb8aa5a053
                        else:
                            join(get_games(sessions_json)[i+1])
                    # print("TEST")
                    join(get_games(sessions_json)[i])

                elif back_rect.collidepoint(event.pos):
                    screen.fill(GREY)
                    exit()
                elif del_rect1.collidepoint(event.pos):
                    delete(get_games(sessions_json)[i])
                elif del_rect2.collidepoint(event.pos):
                    delete(get_games(sessions_json)[i+1])
                elif create_input_rect.collidepoint(event.pos):
                    create_active = True
                    create_color = color_active
                    # need to add the actual code
                elif create_rect.collidepoint(event.pos):
                    create_game(create_text_entry)

                elif launch_rect1.collidepoint(event.pos):
                    print("launch")
                    return get_games(sessions_json)[i]
                elif launch_rect2.collidepoint(event.pos):
                    return get_games(sessions_json)[i+1]
                # elif previous_button_rect.collidepoint(event.pos):
                #     if current_page > 0:
                #         current_page -= 1
                    
                # elif next_button_rect.collidepoint(event.pos):
                #     if current_page < len(get_games(sessions_json)) / 2:
                #         current_page += 1
    
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    pygame.quit()
                    sys.exit()
                if create_active:
                    if event.key == pygame.K_BACKSPACE:
                        create_text_entry = create_text_entry[:-1] # deletes last character
                    else:
                        create_text_entry += event.unicode
        
        pygame.draw.rect(screen, create_color, create_input_rect, 3)

        pygame.draw.rect(screen, RED, back_rect)
        # pygame.draw.rect(screen, GREEN, next_button_rect)
        # pygame.draw.rect(screen, GREEN, previous_button_rect)
        pygame.draw.rect(screen, LIGHT_GREY, create_rect)

        screen.blit(create_text, (420, 325))

        create_text_surface = base_font.render(create_text_entry, True, WHITE)
        screen.blit(create_text_surface, (create_input_rect.x + 5, create_input_rect.y + 5))
        screen.blit(base_text, (350,17))
        screen.blit(back_text, (85, 125))
        # screen.blit(next_text, (655, 685))
        # screen.blit(previous_text, (185, 685))
        screen.blit(create_text_display, (150, 225))
        
        create_input_rect.w = max(600, create_text_surface.get_width() + 10)

        pygame.display.flip()
        clock.tick(FPS)
        
