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


def validate_credentials(username, password):
    if username == "u" and password == "u":
        return True
    else:
        return False


def login():
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    pygame.init()
    pygame.display.set_caption('Splendor')

    clock = pygame.time.Clock()
    screen = pygame.display.set_mode((WIDTH, HEIGHT))  # , pygame.FULLSCREEN

    splendor_text = pygame.image.load('../sprites/splendor-title.png')
    splendor_text = pygame.transform.scale(splendor_text, (500, 200))

    base_font = pygame.font.Font(None, 28)  # font, size

    username_text = ""

    password_text = ""

    username_input_rect = pygame.Rect((150, 350, 200, 35))  # pos_x, pos_y, width, height
    password_input_rect = pygame.Rect((150, 450, 200, 35))

    login_rect = pygame.Rect((350, 600, 200, 70))
    login_text = base_font.render('Login', True, WHITE)

    username_text_display = base_font.render('Username', True, WHITE)
    password_text_display = base_font.render('Password', True, WHITE)

    color_active = pygame.Color(LIGHT_BLUE)
    color_passive = pygame.Color(LIGHT_GREY)
    color_error = pygame.Color(RED)

    username_color = color_passive
    password_color = color_passive

    username_active = False
    password_active = False

    wrong_credentials = False
    
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

            if event.type == pygame.MOUSEBUTTONDOWN:
                wrong_credentials = False
                if login_rect.collidepoint(event.pos):
                    if validate_credentials(username_text, password_text):
                        return
                    else:
                        wrong_credentials = True
                        
                if username_input_rect.collidepoint(event.pos):
                    username_active = True
                    username_color = color_active
                    password_active = False
                    password_color = color_passive
                elif password_input_rect.collidepoint(event.pos):
                    password_active = True
                    password_color = color_active
                    username_active = False
                    username_color = color_passive
                else:
                    if wrong_credentials:
                        username_color = color_error
                        password_color = color_error
                    else:
                        password_color = color_passive
                        username_color = color_passive   
                    username_active = False
                    password_active = False

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    pygame.quit()
                    sys.exit()
                if username_active:
                    if event.key == pygame.K_BACKSPACE:
                        username_text = username_text[:-1]
                    else:
                        username_text += event.unicode
                if password_active:
                    if event.key == pygame.K_BACKSPACE:
                        password_text = password_text[:-1]
                    else:
                        password_text += event.unicode

        screen.fill(GREY)

        screen.blit(splendor_text, (200, 70))

        pygame.draw.rect(screen, username_color, username_input_rect, 3)
        pygame.draw.rect(screen, password_color, password_input_rect, 3)
        pygame.draw.rect(screen, GREEN, login_rect)

        username_text_surface = base_font.render(username_text, True, WHITE)
        screen.blit(username_text_surface, (username_input_rect.x + 5, username_input_rect.y + 5))

        password_text_surface = base_font.render(password_text, True, WHITE)
        screen.blit(password_text_surface, (password_input_rect.x + 5, password_input_rect.y + 5))

        screen.blit(login_text, (420, 625))
        screen.blit(username_text_display, (150, 325))
        screen.blit(password_text_display, (150, 425))

        username_input_rect.w = max(600, username_text_surface.get_width() + 10)
        password_input_rect.w = max(600, password_text_surface.get_width() + 10)

        pygame.display.flip()
        clock.tick(FPS)