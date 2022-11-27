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
def get_players():
    # gets players currently stored in memory
    return ["player1, player3", "player2, player4, player5"]


def session():
    # needs to add game to the list of games
    # some sort of scrolling game inventory
    create_input_rect = pygame.Rect((150, 250, 200, 35))  # pos_x, pos_y, width, height

    create_rect = pygame.Rect((350, 300, 200, 70))
    join_rect = pygame.Rect((350, 600, 200, 70))
    back_rect = pygame.Rect((150, 100, 150, 70))
    previous_button_rect = pygame.Rect((150, 660, 150, 70))
    next_button_rect = pygame.Rect((600, 660, 150, 70))
    delete_text = base_font.render("Delete", True, WHITE)
    back_text = base_font.render('Log Out', True, WHITE)
    create_text = base_font.render('Create', True, WHITE)
    join_text = base_font.render('Join', True, WHITE)
    next_text = base_font.render('Next', True, WHITE)
    previous_text = base_font.render('Previous', True, WHITE)
    create_text_display = base_font.render('Session Name', True, WHITE)
    create_color = color_passive

    create_text_entry = ""

    create_text_surface = base_font.render(create_text_entry, True, WHITE)

    create_active = False
    join_active = False

    game_rect1 = pygame.Rect((150, 450, 400, 55))
    game_rect2 = pygame.Rect((150, 550, 400, 55))
    del_rect1 = pygame.Rect((600, 450, 150, 55))
    del_rect2 = pygame.Rect((600, 550, 150, 55))
    current_page = 0
    wrong_credentials = False # like session somehow invalid
    def join(game):
        while True:
            screen.fill(GREY)
            newtext = base_font.render("Joining " + game +" confirm?", True, WHITE)
 
            pygame.draw.rect(screen, RED, back_rect)

            pygame.draw.rect(screen, GREEN, join_rect)
            screen.blit(newtext, (350, 350))
            screen.blit(back_text, (185, 125))
            screen.blit(join_text, (400, 625))
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:
                    #wrong_credentials = False
                    if join_rect.collidepoint(event.pos):
                        if validate_session(create_text_entry):
                            # move to the waiting room?
                            add_session(create_text_entry)
                            return
                        else:
                            wrong_credentials = True
                    elif back_rect.collidepoint(event.pos):
                        screen.fill(GREY)
                        session()

                    elif join_rect.collidepoint(event.pos) & join_active:
                        pass

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        pygame.quit()
                        sys.exit()
            pygame.display.flip()
            clock.tick(FPS)
    def delete(game):
        pass
    #TODO: add pop up confirmation for create and join
    # connect to server
    while True:
        screen.fill(GREY)
        i = current_page * 2
        last_game_name = base_font.render(get_games()[i] + " / " + get_players()[i], True, WHITE)
        last_game_name = base_font.render(get_games()[i+1] + " / " + get_players()[i+1], True, WHITE)
        pygame.draw.rect(screen, LIGHT_BLUE, game_rect1, 3)
        screen.blit(last_game_name, (game_rect1[0]+20, game_rect1[1]+20))
        pygame.draw.rect(screen, LIGHT_BLUE, game_rect2, 3)
        screen.blit(last_game_name, (game_rect2[0]+20, game_rect2[1]+20))
        pygame.draw.rect(screen, RED, del_rect1)
        pygame.draw.rect(screen, RED, del_rect2)
        screen.blit(delete_text, (del_rect1[0]+30, del_rect1[1]+20))
        screen.blit(delete_text, (del_rect2[0]+30, del_rect2[1]+20))
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.MOUSEBUTTONDOWN:
                wrong_credentials = False
                if game_rect1.collidepoint(event.pos):
                    print("TEST")
                    join(get_games()[i])
                elif game_rect2.collidepoint(event.pos):
                    screen.fill(GREY)
                    join(get_games()[i+1])
                elif back_rect.collidepoint(event.pos):
                    screen.fill(GREY)
                    
                elif create_input_rect.collidepoint(event.pos):
                    create_active = True
                    create_color = color_active
                    # need to add the actual code
                elif join_rect.collidepoint(event.pos) & join_active:
                    pass
                elif previous_button_rect.collidepoint(event.pos):
                    if current_page > 0:
                        current_page -= 1
                    
                elif next_button_rect.collidepoint(event.pos):
                    if current_page < len(get_games()) / 2:
                        current_page += 1

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
        pygame.draw.rect(screen, GREEN, next_button_rect)
        pygame.draw.rect(screen, GREEN, previous_button_rect)
        pygame.draw.rect(screen, LIGHT_GREY, create_rect)

        screen.blit(create_text, (420, 325))

        create_text_surface = base_font.render(create_text_entry, True, WHITE)
        screen.blit(create_text_surface, (create_input_rect.x + 5, create_input_rect.y + 5))

        screen.blit(back_text, (185, 125))
        screen.blit(next_text, (655, 685))
        screen.blit(previous_text, (185, 685))
        screen.blit(create_text_display, (150, 225))
        
        create_input_rect.w = max(600, create_text_surface.get_width() + 10)

        pygame.display.flip()
        clock.tick(FPS)
        
session()