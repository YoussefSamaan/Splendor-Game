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

'''ALL FUNCTIONS HERE HAVE TO BE CHANGED'''
def get_games():
    # gets games currently stored in memory
    return ["game1", "game2"]
def get_joined_games():
    return ["game2"]
def get_players():
    # gets players currently stored in memory
    return ["player1, player3", "player5"]
def get_creator():
    return ["creator1", "creator2"]
def is_game_launched(game):
    return True
def session():
    # needs to add game to the list of games
    # some sort of scrolling game inventory

    current_player = "creator1" # NEED TO CHANGE THIS 

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
    next_text = base_font.render('Next', True, WHITE)
    previous_text = base_font.render('Previous', True, WHITE)
    create_text_display = base_font.render('Session Name', True, WHITE)
    create_color = color_passive

    create_text_entry = ""

    game_rect1 = pygame.Rect((150, 450, 400, 55))
    game_rect2 = pygame.Rect((150, 550, 400, 55))
    del_rect1 = pygame.Rect((655, 450, 90, 55))
    del_rect2 = pygame.Rect((655, 550, 90, 55))
    launch_rect1 = pygame.Rect((655, 450, 90, 55))
    launch_rect2 = pygame.Rect((655, 550, 90, 55))
    leave_rect1 = pygame.Rect((655, 450, 100, 55)) # creator can't leave game 
    leave_rect2 = pygame.Rect((655, 550, 100, 55))
    play_rect1 = pygame.Rect((555, 450, 90, 55))
    play_rect2 = pygame.Rect((555, 550, 90, 55))
    current_page = 0
    wrong_credentials = False # like session somehow invalid
    create_active = False # whether you're clicked on the text input
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
                        session()

                    elif join_rect.collidepoint(event.pos):
                        pass

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        pygame.quit()
                        sys.exit()
            pygame.display.flip()
            clock.tick(FPS)
    
    def delete(game):
        pass
    
    while True:
        screen.fill(GREY)
        i = current_page * 2
        # if we are on i = 3, page =1 we only need to display 1 game
        # len getgames would be 3
        if len(get_games())-i >= 0:
            game_name = base_font.render(get_games()[i] + " / "+ get_creator()[i] + " / " + get_players()[i], True, WHITE)
            screen.blit(game_name, (game_rect1[0]+20, game_rect1[1]+20))
            pygame.draw.rect(screen, LIGHT_BLUE, game_rect1, 3)
            if get_creator()[i] == current_player:
                pygame.draw.rect(screen, RED, del_rect1)
                screen.blit(delete_text, (del_rect1[0]+20, del_rect1[1]+20))
                if is_game_launched(get_games()[i]):
                    pygame.draw.rect(screen, GREEN, play_rect1)
                    screen.blit(play_text, (play_rect1[0]+20, play_rect1[1]+20))
                else:
                    pygame.draw.rect(screen, GREEN, launch_rect1)
                    screen.blit(launch_text, (launch_rect1[0]+20, launch_rect1[1]+20))
            elif get_games()[i] in get_joined_games():
                    pygame.draw.rect(screen, GREEN, launch_rect1)
                    screen.blit(launch_text, (launch_rect1[0]+10, launch_rect1[1]+20))

            
        if len(get_games())-i >= 1:
            game_name2 = base_font.render(get_games()[i+1] + " / " + get_creator()[i+1] + " / " + get_players()[i+1], True, WHITE)
            pygame.draw.rect(screen, LIGHT_BLUE, game_rect2, 3)
            screen.blit(game_name2, (game_rect2[0]+20, game_rect2[1]+20))
            if get_creator()[i+1] == current_player:
                pygame.draw.rect(screen, RED, del_rect2)
                screen.blit(delete_text, (del_rect2[0]+20, del_rect2[1]+20))
                if is_game_launched(get_games()[i+1]):
                    pygame.draw.rect(screen, GREEN, play_rect2)
                    screen.blit(play_text, (play_rect2[0]+20, play_rect2[1]+20))
                else:
                    pygame.draw.rect(screen, GREEN, launch_rect2)
                    screen.blit(launch_text, (launch_rect2[0]+10, launch_rect2[1]+20))
            elif get_games()[i+1] in get_joined_games():
                    pygame.draw.rect(screen, GREEN, launch_rect2)
                    screen.blit(launch_text, (launch_rect2[0]+10, launch_rect2[1]+20))

        # add code to make sure only creator can delete game 
        # add code to make sure only joined player can leave game 

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
                elif del_rect1.collidepoint(event.pos):
                    delete(get_games()[i])
                elif del_rect2.collidepoint(event.pos):
                    delete(get_games()[i+1])
                elif create_input_rect.collidepoint(event.pos):
                    create_active = True
                    create_color = color_active
                    # need to add the actual code
                elif join_rect.collidepoint(event.pos):
                    pass
                # elif previous_button_rect.collidepoint(event.pos):
                #     if current_page > 0:
                #         current_page -= 1
                    
                # elif next_button_rect.collidepoint(event.pos):
                #     if current_page < len(get_games()) / 2:
                #         current_page += 1

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
        
session()