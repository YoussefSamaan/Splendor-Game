import os
import pygame
import sys

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



base_font = pygame.font.Font(None, 28)  # font, size

def validate_session(name):
    return True # need to add the actual code
def add_session(name):
    return True
def validate_join(player):
    return True # likewise

def get_games():
    # gets games currently stored in memory
    return ["game1", "game2"]

def session():
    # needs to add game to the list of games
    # some sort of scrolling game inventory
    create_input_rect = pygame.Rect((150, 250, 200, 35))  # pos_x, pos_y, width, height

    create_rect = pygame.Rect((350, 300, 200, 70))
    join_rect = pygame.Rect((350, 600, 200, 70))
    back_rect = pygame.Rect((150, 100, 150, 70))
    back_text = base_font.render('Log Out', True, WHITE)
    create_text = base_font.render('Create', True, WHITE)
    join_text = base_font.render('Join', True, WHITE)
    create_text_display = base_font.render('Session Name', True, WHITE)
    create_color = color_passive

    create_text_entry = ""

    create_text_surface = base_font.render(create_text_entry, True, WHITE)

    create_active = False

    wrong_credentials = False # like session somehow invalid


    while True:
        screen.fill(GREY)
        last_game_rect = pygame.Rect((150, 450, 200, 55))
        last_game_location = (150, 450)
        for game in get_games():
            #print(game)
            last_game_name = base_font.render(game, True, WHITE)
            pygame.draw.rect(screen, LIGHT_BLUE, last_game_rect, 3)
            screen.blit(last_game_name, (last_game_location[0]+20, last_game_location[1]+20))
            last_game_location = (150, last_game_location[1] + 100)
            last_game_rect = pygame.Rect((150, last_game_location[1], 200, 55))
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.MOUSEBUTTONDOWN:
                wrong_credentials = False
                if create_rect.collidepoint(event.pos):
                    if validate_session(create_text_entry):
                        # move to the waiting room?
                        add_session(create_text_entry)
                        return
                    else:
                        wrong_credentials = True
                elif back_rect.collidepoint(event.pos):
                    screen.fill(GREY)
                    
                elif create_input_rect.collidepoint(event.pos):
                    create_active = True
                    create_color = color_active

                else:
                    if wrong_credentials:
                        create_color = color_error
                        
                    else:
                        create_color = color_passive
                        
                    create_active = False
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    pygame.quit()
                    sys.exit()
                if create_active:
                    if event.key == pygame.K_BACKSPACE:
                        create_text_entry = create_text[:-1] # deletes last character
                    else:
                        create_text_entry += event.unicode


        
        pygame.draw.rect(screen, create_color, create_input_rect, 3)

        pygame.draw.rect(screen, RED, back_rect)
        pygame.draw.rect(screen, LIGHT_GREY, create_rect)

        screen.blit(create_text, (420, 325))

        create_text_surface = base_font.render(create_text_entry, True, WHITE)
        screen.blit(create_text_surface, (create_input_rect.x + 5, create_input_rect.y + 5))

        screen.blit(back_text, (185, 125))
        screen.blit(create_text_display, (150, 225))
        
        create_input_rect.w = max(600, create_text_surface.get_width() + 10)

        pygame.display.flip()
        clock.tick(FPS)
        
session()