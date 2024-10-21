import pygame
import random
from random import randint
import sys
import time

#Global variables:

pygame.init()
#initalizes pygame
clock = pygame.time.Clock()

make_coin = pygame.USEREVENT

pygame.time.set_timer(make_coin, random.randrange(2000, 3000))

make_pipe = pygame.USEREVENT + 0
#creates a userevent for us to generate obstacles

pygame.time.set_timer(make_pipe+2, random.randrange(2000, 3000))

pygame.time.set_timer(make_pipe+1, random.randrange(5000, 7000))
#generates random pipes

width = 400
height = 600

SCORE_BOARD_CLASSIC = {}
SCORE_BOARD_SPEED = {}
SCORE_BOARD_COINS = {}

#collects info for score board

running = False

#create flappy ask a bird class

screen = pygame.display.set_mode((width, height))

#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 

#ALL CLASSES

class Bird():
    def __init__(self, x, y):
        self.x, self.y = x, y
        self.speed = 0
        #self.y = y

        self.image = pygame.image.load("bird.png")
        self.image = pygame.transform.scale(self.image, (50, 50))

        self.mask = pygame.mask.from_surface(self.image)

    def draw(self, main1, main2):
        screen.blit(self.image,(main1, main2))
        #creating and printing the flappy bord
        self.rect = pygame.Rect(main1, main2, 48, 48)
        self.hitbox = (main1, main2, 48, 48)

         #credit to this for helping me do masks
        #https://gamedev.stackexchange.com/questions/194752/cant-check-collision-between-a-mask-and-a-rect-in-pygame?noredirect=1#comment353233_194752
        
#creates the flappy bird
class Pipes():
    #creates an image for pipe
    def __init__(self):
        self.x, self.y = 400, random.randint(-450, -120)
        #self.y = random.randint(-400, 0)
        self.pipe1 = pygame.image.load("pipe copy.png")
        self.pipe1.set_colorkey((250,250,250))
        self.pipe2 = pygame.image.load("pipe copy 2.png")
        self.pipe2.set_colorkey((250,250,250))
        #takes out  all the whites from the images
        self.pipe1 = pygame.transform.scale(self.pipe1, (60, 500))
        self.pipe2 = pygame.transform.scale(self.pipe2, (60, 500))
        self.score = pygame.image.load("transparent.png")
        self.score = pygame.transform.scale(self.score, (10, 600))
        #adjusts the scale of the pipes
        
        self.width = self.pipe1.get_width()
        self.height2 = self.pipe2.get_height()
        self.slay = self.height2-(self.y * -1)
        #so we can adjust the bottom pipe to be exactly 180 pixels away from other pipe

        self.mask1 = pygame.mask.from_surface(self.pipe1)
        self.mask2 = pygame.mask.from_surface(self.pipe2)
        self.mask_S = pygame.mask.from_surface(self.score)

        #Puts a mask over both pipes so we can use the collision method

    def draw(self):
        screen.blit(self.pipe2, (self.x, self.y))
        screen.blit(self.pipe1, (self.x, self.slay + 180))
        screen.blit(self.score, (self.x+59, 0))
        #Draws the pipes
#creates the pipes
        
class Score():
    def __init__(self):
        self.x = 250
        self.y = 50

    def draw_score(self, score):
        sco = str(score)
        sco_txt = pygame.font.SysFont('Corbel', 50)
        print_sco = sco_txt.render("Pipes: " + sco, True, (0,0,0))
        screen.blit(print_sco, (self.x, self.y))
        #prints score on game screen as it updates
#coin items
class Coins():

   def __init__(self):
        self.x, self.y = 400, random.randint(100, 300)

        self.image = pygame.image.load("coin.png")
        self.image = pygame.transform.scale(self.image, (20, 20)) 

        self.width = self.image.get_width()
       
        self.height2 = self.image.get_height()
        self.mask = pygame.mask.from_surface(self.image)
        #a mask over coins so we can do collide 

   def draw(self):
        screen.blit(self.image,(self.x, self.y))
        
#Collects coin score
        
class Coin_score():
    def __init__(self):
        self.x = 250
        self.y = 90

    def draw_score(self, coin_score):
        score = str(coin_score)
        score_txt = pygame.font.SysFont('Corbel', 50)
        print_score = score_txt.render("Coins:" + score, True, (0,0,0))
        screen.blit(print_score, (self.x, self.y))
        #shows users their current coin score

class User():
    def __init__(self):
        self.score = 0
        #set score to 0
        self.user = ""

    def read(self):
        
        board = open("ScoreBoard.txt").readlines()
        
        scores_classic = []
        scores_speed = []
        scores_coins = []
        thing = 0


       #append each list to scores
        for i in board:
            game, name, score = i.split()
            if game == "classic":
               scores_classic.append([name, score])
            if game == "speed":
               scores_speed.append([name, score])
            if game == "coins":
               scores_coins.append([name, score])

       # adds score to respective dict
        if len(scores_classic) <= 0:
           pass
        else:
            for items in scores_classic:
                if len(items) <= 0:
                #empty items are not added
                    pass
                else:
                    SCORE_BOARD_CLASSIC[items[0]] = items[1]
      
        if len(scores_speed) <= 0:
           pass
        else:
           for items in scores_speed:
                if len(items) <= 0:
                #empty items are not added
                    pass
                else:
                    SCORE_BOARD_SPEED[items[0]] = items[1]
      
        if len(scores_coins) <= 0:
            #check if the list is empty so we don't throw and error
           pass
        else:
           for items in scores_coins:
                if len(items) <= 0:
                #empty items are not added
                    pass
                else:
                    SCORE_BOARD_COINS[items[0]] = items[1]
  
            #goes through the scores and adds to the global dict

    def write(self, score, mode, username):
        self.read()
        #creates our dics
        self.user = username
            
        # Replace highscore if username is in dict and score is higher than current highscore
        # Add username and score to dict if username is not in dict
        if mode == "classic":
           if username in SCORE_BOARD_CLASSIC:
               if score > int(SCORE_BOARD_CLASSIC.get(username)):
                   SCORE_BOARD_CLASSIC[username] = score
           elif username not in SCORE_BOARD_CLASSIC:
               SCORE_BOARD_CLASSIC[username] = score

        if mode == "speed":
           if username in SCORE_BOARD_SPEED:
               if score > int(SCORE_BOARD_SPEED.get(username)):
                   SCORE_BOARD_SPEED[username] = score
           elif username not in SCORE_BOARD_SPEED:
               SCORE_BOARD_SPEED[username] = score

        if mode == "coins":
           if username in SCORE_BOARD_COINS:
               if score > int(SCORE_BOARD_COINS.get(username)):
                   SCORE_BOARD_COINS[username] = score
           elif username not in SCORE_BOARD_COINS:
               SCORE_BOARD_COINS[username] = score

       # Write scores into file
        
        with open("ScoreBoard.txt", "w") as file:
           for key, value in SCORE_BOARD_CLASSIC.items():
               file.write("%s %s %s\n" % ("classic", key, value))

           for key, value in SCORE_BOARD_SPEED.items():
               file.write("%s %s %s\n" % ("speed", key, value))

           for key, value in SCORE_BOARD_COINS.items():
               file.write("%s %s %s\n" % ("coins", key, value))
                #writes the user into the scores
        #writing in files:
        #https://www.geeksforgeeks.org/write-a-dictionary-to-a-file-in-python/

#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              
#First User input screen:
                
def UserMenu():
    background = pygame.image.load("usermenu.png")
    background = pygame.transform.scale(background, (400, 600 ))
    menu = True
    user_font = pygame.font.Font(None, 32)
    #creates a font object that can be rendered which then can be blited
    user_inp = pygame.Rect(150,280, 320, 50)
    user_t = ""
    #records user input
    while menu:
        
        
        screen.blit(background, (0, 0))
        #displays downloaded usermenu background image

        mouse_pos = pygame.mouse.get_pos()
        #gets mouse position

        user_txt = pygame.font.SysFont('Comic Sans', 40)
        user = user_txt.render("Welcome User!", True, (0, 0, 0))
        #renders Font objects
        user_rect = user.get_rect(center=(200, 140))
        #gets rectangle based on user and centers

        instr = pygame.font.SysFont("Arial", 20)
        instr_txt = instr.render("Please enter a username", True, (0, 0, 0))
        
        screen.blit(instr_txt, (85, 180))
        #shows instr_txt
        
        rect_txt = pygame.font.SysFont('Arial', 30)
        text_rect = rect_txt.render("Submit", True, (250, 250, 250))
        
        rectangle = pygame.Rect(100, 400, 200, 60)
        pygame.draw.rect(screen, (0, 0, 0), rectangle)
        #gets rectangle that can be used to allow collide point
        
        screen.blit(text_rect, (150, 410)) 
        collide = rectangle.collidepoint(mouse_pos)
        #records if mouse position is collided with my rectangle
         
        screen.blit(user, user_rect)
        events = pygame.event.get()
        
        for event in events:
            if event.type == pygame.QUIT:
                menu = False
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_BACKSPACE:
                    user_t = user_t[:-1]
                    #checks is the user keydown was backspace and then deletes the last item in string
                else:
                    user_t+= event.unicode
                    #adds the letter the user input into user_t
            if event.type == pygame.MOUSEBUTTONDOWN:
                if collide:
                    loser_t = user_t.strip()
                    if len(loser_t) <= 0:
                        retry = rect_txt.render("Please enter a name", True, (250, 250, 250))
                        screen.blit(retry, (55, 355))
                        pygame.display.flip()
                        pygame.time.delay(2000)
                    else:
                        Mode(user_t)
                        menu = False
                    #sees if users press was on top of our rectangle and then does something
        pygame.draw.rect(screen, (255, 255, 255), user_inp)
        txt_surface = user_font.render(user_t, True, (0,0,0))
        #more font rendering
        screen.blit(txt_surface, (user_inp.x+5, user_inp.y+5))
        #adjusts the rectangle around our text to adjust as the user types
        user_inp.w = max(100, txt_surface.get_width()+10)

        pygame.display.flip()

        clock.tick(60)
        #makes sure there are 60 frames per second

        #FOR THE TEXT BOX PYGAME CODE we used:
        #https://www.geeksforgeeks.org/how-to-create-a-text-input-box-with-pygame/
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              
#Mode choosing Screen:
        
def Mode(username):
    background = pygame.image.load("mode.png")
    background = pygame.transform.scale(background, (400, 600 ))

    menu = True
    while menu:
        

        screen.blit(background, (0, 0))
        #displays downloaded mode background image

        mouse_pos = pygame.mouse.get_pos()
        #grabs mouse position

        user_txt = pygame.font.SysFont('Comic Sans', 40)
        user = user_txt.render("Select a Mode", True, (0, 0, 0))
        user_rect = user.get_rect(center=(200, 110))
        #creates text for title and shows it on the screen

        mode1t = pygame.font.SysFont('Ariel', 50)
        mode1 = mode1t.render("Classic", True, (0, 0, 0))
        mode1_rect = mode1.get_rect(center=(200, 220))
        m1_rectangle = pygame.Surface((200,60))
        m1_rectangle.fill((255,255,255))
        #also creates text and shows it on the screen
        
        rect1 = pygame.Rect(100,165,200,70)

        #FIRST MODE VISUAL CODE ^^^^^

        mode2t = pygame.font.SysFont('Ariel', 50)
        mode2 = mode2t.render("Speed", True, (0, 0, 0))
        mode2_rect = mode2.get_rect(center=(200, 320))
        m2_rectangle = pygame.Surface((200,60))
        m2_rectangle.fill((255,255,255))

        rect2 = pygame.Rect(100,265,200,70)

        #SECOND MODE VISUAL CODE ^^^^^
        
        mode3t = pygame.font.SysFont('Ariel', 50)
        mode3 = mode3t.render("Coin Hunt", True, (0, 0, 0))
        mode3_rect = mode3.get_rect(center=(200, 420))
        m3_rectangle = pygame.Surface((200,60))
        m3_rectangle.fill((255,255,255))

        rect3 = pygame.Rect(100,365,200,70)

        #THIRD MODE VISUAL CODE ^^^^^^

        boardT = pygame.font.SysFont('Ariel', 50)
        board = boardT.render("Score Board", True, (0, 0, 0))
        board_rect = board.get_rect(center=(200, 520))
        m4_rectangle = pygame.Surface((200,60))
        m4_rectangle.fill((255,255,255))

        rect4 = pygame.Rect(100,465,200,70)

        #SCOREBOARD VISAL CODE ^^^^^^
        
        #creating a lot of rectangles to act as buttons ^^^^^
        
        screen.blit(user, user_rect)
        screen.blit(m1_rectangle, (100,185))
        screen.blit(mode1,mode1_rect)
        
        screen.blit(m2_rectangle, (100,285))
        screen.blit(mode2,mode2_rect)

        screen.blit(m3_rectangle, (100,385))
        screen.blit(mode3,mode3_rect)

        screen.blit(m4_rectangle, (100,485))
        screen.blit(board,board_rect)
        
        #showing all the text and rectangles we made on the game screen
        
        events = pygame.event.get()
        
        for event in events:
            if event.type == pygame.QUIT:
                menu = False
                #quits screen
            if event.type == pygame.MOUSEBUTTONDOWN:
                 collide_check1 = rect1.collidepoint(mouse_pos)
                 collide_check2 = rect2.collidepoint(mouse_pos)
                 collide_check3 = rect3.collidepoint(mouse_pos)
                 collide_check4 = rect4.collidepoint(mouse_pos)           
                
                 
                 if collide_check1:
                    username = username.strip()
                    Classic(username, "classic")
                    menu = False
                    #different collide checks to go to different game modes
                    
                 if collide_check2:
                    username = username.strip()
                    Classic(username, "speed")
                    menu = False

                 if collide_check3:
                    username = username.strip()
                    Coin_hunt(username)
                    menu = False

                 if collide_check4:
                    Score_Screen()
                                        
        pygame.display.update()
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#Scoreboard screen:

def Score_Screen():
    scoreboard = pygame.image.load("scores.png")
    scoreboard = pygame.transform.scale(scoreboard, (400, 600))
    S_S = True
    while S_S:
        screen.blit(scoreboard,(0,0))
        mouse_pos = pygame.mouse.get_pos()

        #displays downloaded scoreboard background image

        s_u = User()
        s_u.read()

        slay_queen = pygame.font.SysFont('Corbel', 30)
        slay_names = pygame.font.SysFont('Corbel', 40)
        slay_title = pygame.font.SysFont('Corbel', 50)

        title = slay_title.render("Top 3 Players", True, (0, 0, 0))

        boardT = pygame.font.SysFont('Corbel', 30)
        board = boardT.render("Return", True, (250, 250, 250))
        board_rect = board.get_rect(center=(200, 525))
        m4_rectangle = pygame.Surface((200,50))
        m4_rectangle.fill((0,0,0))
        rect4 = pygame.Rect(100,500,200,50)
        #Back Button visual code

        screen.blit(title, (88, 20))
        #regular visuals for letters with no buttons

        screen.blit(m4_rectangle, (100,500))
        screen.blit(board,board_rect)
        #BUTTON BACK BUTTON BUTTON BUTTON BACK

        collide = rect4.collidepoint(mouse_pos)
        
        sorted_scores_cl = dict(sorted(SCORE_BOARD_CLASSIC.items(), key=lambda item:int(item[1]), reverse = True))
        sorted_scores_sp = dict(sorted(SCORE_BOARD_SPEED.items(), key=lambda item:int(item[1]), reverse = True))
        sorted_scores_co = dict(sorted(SCORE_BOARD_COINS.items(), key=lambda item:int(item[1]), reverse = True))

        #sorts the dictonary of scores based on the users with the highest scores

        all_sorted_scores = [sorted_scores_cl, sorted_scores_sp, sorted_scores_co]

        names = []
        scores = []

        for lst in all_sorted_scores:
           if len(lst) < 3:
                names.append(list(lst.keys()))
                scores.append(list(lst.values()))
           else:
                names.append(list(lst.keys())[:3])
                scores.append(list(lst.values())[:3])

        #show the lists on the screen

        y_pos = 100
        y_pos_2 = 100

        # classic mode leaderboard
        slay_head = pygame.font.SysFont('Corbel', 35 )
        classic_head = slay_head.render("Classic", True, (0, 0, 0))
        screen.blit(classic_head, (158, y_pos-30))
        for n in names[0]:
            n = n[:3].upper()
            slay = slay_queen.render (n, True, (0, 0, 0))
            screen.blit(slay, (100, y_pos))
            y_pos += 30
        for s in scores[0]:
            slayscores = slay_queen.render (".................. " + str(s), True, (0, 0, 0))
            screen.blit(slayscores, (155, y_pos_2))
            y_pos_2 += 30
      


       # speed mode leaderboard
        y_pos += 30
        speed_head = slay_head.render("Speed", True, (0, 0, 0))
        screen.blit(speed_head, (160, y_pos))
        y_pos += 30
        y_pos_2 += 60
        for n in names[1]:
            n = n[:3].upper()
            slay = slay_queen.render (n, True, (0, 0, 0))
            screen.blit(slay, (100, y_pos))
            y_pos += 30
        for s in scores[1]:
            slayscores = slay_queen.render (".................. " + str(s), True, (0, 0, 0))
            screen.blit(slayscores, (155, y_pos_2))
            y_pos_2 += 30
      
       # coins mode leaderboard
        y_pos += 30
        coins_head = slay_head.render("Coins", True, (0, 0,0))
        screen.blit(coins_head, (160, y_pos))
        y_pos += 30
        y_pos_2 += 60
        for n in names[2]:
            n = n[:3].upper()
            slay = slay_queen.render (n, True, (0, 0, 0))
            screen.blit(slay, (100, y_pos))
            y_pos += 30
        for s in scores[2]:
            slayscores = slay_queen.render (".................. " + str(s), True, (0, 0, 0))
            screen.blit(slayscores, (155, y_pos_2))
            y_pos_2 += 30
          


        events = pygame.event.get()
        for event in events:
           if event.type == pygame.QUIT:
                S_S = False
               #quits screen
           if event.type == pygame.MOUSEBUTTONDOWN:
                if collide:
                    S_S = False
               #returns to Mode
              
                           
        pygame.display.flip()
        pygame.display.update
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              
#Play Again Screen:
        
def Play_Again(username):
      
        pA = True
        while pA == True:

           screen.fill((0,0,0))

           mouse_pos = pygame.mouse.get_pos()
      
           yes_t = pygame.font.SysFont('Corbel', 50)
           yes = yes_t.render("TRY AGAIN", True, (0, 0, 0))
           yes_rect = yes.get_rect(center=(200, 200))
           yes_rectangle = pygame.Surface((200,70))
           yes_rectangle.fill((255,255,255))

           screen.blit(yes_rectangle, (100,165))
           screen.blit(yes,yes_rect)

           yesrect = pygame.Rect(100,165,200,70)

           no_t = pygame.font.SysFont('Corbel', 50)
           no = no_t.render("END GAME", True,(0, 0, 0))
           no_rect = no.get_rect(center=(200, 300))
           no_rectangle = pygame.Surface((200,70))
           no_rectangle.fill((255,255,255))

           screen.blit(no_rectangle, (100,265))
           screen.blit(no,no_rect)

           norect = pygame.Rect(100,265,200,70)

           events = pygame.event.get()


           for event in events:

              if event.type == pygame.QUIT:
                pA = False
                #quits screen
           
              if event.type == pygame.MOUSEBUTTONDOWN:
                       collide_check1 = yesrect.collidepoint(mouse_pos)
                       collide_check2 = norect.collidepoint(mouse_pos)
                       
                       if collide_check1:
                          Mode(username)
                          pA = False
                          
                       if collide_check2:
                          Score_Screen()
                          pA = False
                          
           pygame.display.update()

#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              
#CLASSIC game mode
#Also contains the speed mode

# a main loop that will run my game
def Classic(U_name, mode):
    running = True
    main_y = 300
    main_x = 70
    obstacles = []
    obstacles_unpassed = []
    background = pygame.image.load("background.png")
    background = pygame.transform.scale(background, (900, 630))
    #download in background
    score = 0
    jump = 80 ## TEMPORARY
    gravity = 0.38
    vel = gravity
    accel = 0
    jump_time = time.time()
    jumping = False
    # variables for speed mode:
    last_pipe = time.time()
    t_btwn = 3
    #creating out class objects to be used:
    Flappy = Bird(main_x, main_y)
    MyScore = Score()
    while running:
        screen.fill((0,0,0))
        #fills screen black
        #DT = clock.tick(25)
        
        screen.blit(background, (0,0))
        #shows background variable

        Flappy.draw(main_x, main_y)
        #creates bird at new position

        for obstacle in obstacles:
            #screen.blit(obstacle.trans, (obstacle.x, obstacle.y))
            obstacle.draw()
            #creates and draws the obstacle

        MyScore.draw_score(score)
        
        pygame.display.update()
        
        events = pygame.event.get()
        #gets the events or the user interaction
        main_y += vel
        vel += accel
        #has out bird consistantly falling

        for event in events:
            if event.type == pygame.QUIT:
                running = False
                #quits screen

            if event.type == pygame.KEYDOWN:

                   # make bird go up when spacebar is pressed
                if event.key == pygame.K_SPACE and jumping == False and vel > 0:
                   jump_time = time.time()
                   vel = -0.35
                   jumping = True


                elif event.key == pygame.K_SPACE and jumping == True:
                   jump_time = time.time()
                   if vel <= 0:
                       vel -= .5
                   elif vel > 0:
                       vel = 0.385
              #makes bird go up when spacebar is pressed
                       
                     #moves pipes across the screen and deletes it once it is off screen
            if mode == "classic":
                if event.type == make_pipe+2:
                    clock.tick(60)
                    obstacles.append(Pipes())
                    obstacles_unpassed.append(Pipes())
           # Creates pipe and adds the obstacle to the list at decreasing intervals
            if mode == "speed":
                clock.tick(60)
                if time.time() - last_pipe > t_btwn:
                    obstacles.append(Pipes())
                    obstacles_unpassed.append(Pipes())
                    last_pipe = time.time()
                    if t_btwn > 1:
                        t_btwn -= 0.1
                    elif t_btwn > .5:
                        t_btwn -= 0.05   
                  
                #adds the obstacle to the list at random interval

        for obstacle in obstacles:
            obstacle.x -= 0.2
            if obstacle.x < obstacle.width * -1:
                obstacles.pop(obstacles.index(obstacle))

        for obstacle in obstacles_unpassed:
               obstacle.x -= 0.2
               if obstacle.x < 35:
                   score += 1
                   obstacles_unpassed.pop(obstacles_unpassed.index(obstacle))
        #allows the score to go up
            
        pygame.display.flip()

        #provides boundaries so our user cannot just fly out of the atmosphere
        if (main_y >= screen.get_height() - Flappy.image.get_height()) or main_y <= 0:
            screen.fill((250,0,0))
            pygame.display.flip()
            pygame.time.delay(2000)
            Flap = User()
            Flap.write(score, mode, U_name)
            Play_Again(U_name)
            running = False
            
        for obstacle in obstacles:
            offset1 = [obstacle.x - main_x, (obstacle.slay +180) - main_y]
            offset2 = [obstacle.x - main_x, obstacle.y - main_y]
            offset3 = [obstacle.x+60 - main_x, 0 - main_y]
            #finds the distance between the two objects

            if Flappy.mask.overlap(obstacle.mask1, offset1):
                Flappy.draw(main_x, main_y)
                pygame.display.flip()
                screen.fill((250,0,0))
                pygame.display.flip()
                pygame.time.delay(2000)
                Flap = User()
                Flap.write(score, mode, U_name)
                Play_Again(U_name)
                #writes users stats into dic and into score file
                running = False
                
                #these two ifs detect collision so we can have our character die
            if Flappy.mask.overlap(obstacle.mask2, offset2):
                screen.fill((250,0,0))
                pygame.display.flip()
                pygame.time.delay(2000)
                Flap = User()
                Flap.write(score, mode, U_name)
                #same as previous
                Play_Again(U_name)

                running = False
                
        if time.time() > jump_time and time.time() <= jump_time + .36 and jumping == True:
            accel += .000005
        #ends jump acceleration and makes bird fall again

        if time.time() > jump_time +.36 and jumping == True:
           accel += .215
           jumping = False

           #speeds falling acceleration
  
        if vel > gravity:
           vel = gravity
           accel = 0
           jumping = False
        #keeps the bird from falling faster than gravity


            #credit to this for helping me do masks
            #https://gamedev.stackexchange.com/questions/194752/cant-check-collision-between-a-mask-and-a-rect-in-pygame?noredirect=1#comment353233_194752
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                              
#Coin hunt mode:
     
def Coin_hunt(U_name):
    """
    Function that runs a version of the game with coins implemented
    """
    running = True
    
    #Variables:
    main_y = 300
    main_x = 70
    background = pygame.image.load("background.png")
    background = pygame.transform.scale(background, (900, 630))
    #download in background
    score = 0
    coin_score = 0
    jump = 80 ## TEMPORARY
    gravity = 0.38
    vel = gravity
    accel = 0
    jump_time = time.time()
    jumping = False
    Flappy = Bird(main_x, main_y)
    MyScore = Score()
    obstacles = []
    obstacles_unpassed = []
    CoinScore = Coin_score()
    coinsli = []
    #coin list and coin scores added
    while running:
         
        screen.blit(background, (0,0))
        #shows background variable
        
        Flappy.draw(main_x, main_y)
        #creates bird at new position

        for obstacle in obstacles:
            obstacle.draw()
            #creates and draws the obstacle
        MyScore.draw_score(score)
        #creates and draws score
        
         # only draws coins if they don't overlap pipes
        for coin in coinsli:
           for obstacle in obstacles:

               offset1 = [obstacle.x - coin.x, (obstacle.slay +180) - coin.y]
               offset2 = [obstacle.x - coin.x, obstacle.y - coin.y]
               

               if coin.mask.overlap(obstacle.mask1, offset1) or coin.mask.overlap(obstacle.mask2, offset2):
                  coinsli.pop(coinsli.index(coin))
            
              
           coin.draw()
        #creates and draws coin score/count          
        CoinScore.draw_score(coin_score)
        pygame.display.update()
                       
        events = pygame.event.get()
        #gets the events or the user interaction
        main_y += vel
        vel += accel
        #has our bird consistantly falling

        for event in events:
            if event.type == pygame.QUIT:
                running = False
                #quits screen

            if event.type == pygame.KEYDOWN:

                   # make bird go up when spacebar is pressed
                if event.key == pygame.K_SPACE and jumping == False and vel > 0:
                   jump_time = time.time()
                   vel = -0.35
                   jumping = True


                elif event.key == pygame.K_SPACE and jumping == True:
                   jump_time = time.time()
                   if vel <= 0:
                       vel -= .5
                   elif vel > 0:
                       vel = 0.385
                    #makes bird go up when spacebar is pressed

            if event.type == make_pipe+1:
                clock.tick(60)
                obstacles.append(Pipes())
                obstacles_unpassed.append(Pipes())

            if event.type == make_coin:
               
               coinsli.append(Coins())
                     
                #adds the obstacle to the list at random interval
       
        for obstacle in obstacles:
            obstacle.x -= 0.2
            if obstacle.x < obstacle.width * -1:
                obstacles.pop(obstacles.index(obstacle))
        # updates the score if obstacle has been passed
        for obstacle in obstacles_unpassed:
               obstacle.x -= 0.2
               if obstacle.x < 35:
                   score += 1
                   obstacles_unpassed.pop(obstacles_unpassed.index(obstacle))
                
        #moves pipes across the screen and deletes it once it is off screen

        for coin in coinsli:
            coin.x -= 0.2
            if coin.x < -10 :
                coinsli.pop(coinsli.index(coin))
        #moves coins across the screen and deletes it once it is off screen         
        CoinScore.draw_score(coin_score)
        MyScore.draw_score(score)
            
        pygame.display.flip()

        if (main_y >= screen.get_height() - Flappy.image.get_height()) or main_y <= 0:
            screen.fill((250,0,0))
            score = score + coin_score
            pygame.display.flip()
            pygame.time.delay(2000)
            Flap = User()
            Flap.write(score,"coins", U_name)
            Play_Again(U_name)

            running = False
        #Adds borders so bird can't fly out of window
            
        for obstacle in obstacles:
            offset1 = [obstacle.x - main_x, (obstacle.slay +180) - main_y]
            offset2 = [obstacle.x - main_x, obstacle.y - main_y]
            offset3 = [obstacle.x+60 - main_x, 0 - main_y]
            #finds the distance between the two objects

            if Flappy.mask.overlap(obstacle.mask1, offset1):
                score = score + coin_score
                Flappy.draw(main_x, main_y)
                pygame.display.flip()
                screen.fill((250,0,0))
                pygame.display.flip()
                pygame.time.delay(1000)
                Flap = User()
                Flap.write(score, "coins", U_name)
                Play_Again(U_name)
               
                running = False
                
        #these two ifs detect collision so we can have our character die and update final score
            if Flappy.mask.overlap(obstacle.mask2, offset2):
                score = score + coin_score
                screen.fill((250,0,0))
                pygame.display.flip()
                pygame.time.delay(1000)
                Flap = User()
                Flap.write(score, "coins", U_name)
                Play_Again(U_name)

                running = False  
                     
        for coin in coinsli: 

            if Flappy.mask.overlap(coin.mask, (coin.x - Flappy.rect.x, coin.y - Flappy.rect.y )):
                Flappy.draw(main_x, main_y)
               #draws bird

                coin_score += 1
                coinsli.pop(coinsli.index(coin))
                #destroys the coin shown



        if time.time() > jump_time and time.time() <= jump_time + .36 and jumping == True:
            accel += .000005

        #stops acceleration and begins decesnt 

        if time.time() > jump_time +.36 and jumping == True:
           accel += .215
           jumping = False

        #makes falling go faster
  
        if vel > gravity:
           vel = gravity
           accel = 0
           jumping = False

#Running Game

UserMenu()

pygame.display.quit()
pygame.quit()
# clean up after the game is done running
