import sys, math
#import and init pygame
import pygame
pygame.init() 

#create the screen
w=600
h=600
window = pygame.display.set_mode((w, h)) 

#draw a line - see http://www.pygame.org/docs/ref/draw.html for more 
c_white = (255,255,255)
c_red = (255,0,0)
c_blue = (0,0,255)

p_a = (300,200)
p_b = (200,400)
p_b1 = (250,450)
p_c = (400,400)

objectDraw = []
const = 0

def redraw():
	if (len(objectDraw) > 0):
		const = 3.0/math.pow(len(objectDraw),1)
#		const = 3.0/math.pow(len(objectDraw),8)
	
	print const
	c = 0.0
	
	for x in range(len(objectDraw)):
		for y in range(x,len(objectDraw)):
			c = c + math.sqrt(math.pow((objectDraw[x][0]-objectDraw[y][0]),2)+math.pow((objectDraw[x][1]-objectDraw[y][1]),2))
	
	for i in range(w):
		for j in range(h):
			k = 0.0
			for x in objectDraw:
				k = k + math.sqrt(math.pow((x[0]-i),2)+math.pow((x[1]-j),2))
			if (k <= c*const) :
				pygame.draw.line(window, c_red, (i, j), (i, j))
	
	if (len(objectDraw) > 0):
		last = objectDraw[0]
		for x in range(1,len(objectDraw)):
			pygame.draw.line(window, c_white, last, objectDraw[x])
			last = objectDraw[x]
		pygame.draw.line(window, c_white, last, objectDraw[0])
	
	#draw it to the screen
	pygame.display.flip() 



#input handling (somewhat boilerplate code):
while True: 
   for event in pygame.event.get(): 
      if event.type == pygame.QUIT: 
          sys.exit(0) 
      if event.type == pygame.MOUSEBUTTONUP: 
	  objectDraw.append(event.pos)
	  redraw()
	  print event.pos
#      else: 
          #print event 
