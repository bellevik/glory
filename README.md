#Zombie Apocalypse

##What is Zombie Apocalypse?
Zombie Apocalypse is an awesome Android game where you play as a normal guy in a city where humans turn into zombies!
Run, shoot, kill, take shelter, put other humans in the way - yah, do all it takes to survive as long as possible!

##How does it work?
###User interface
We put a great amount of thought into how the game character should be controlled.
The result is joysticks, two of them, one for movement control and one for the use of weapons.

###The AI
####Zombies
Zombies chase the closest human, given a certain range, it is as simple as that.

####Humans
Humans flee zombies, given a certain range, in an intelligent way.
You could describe it as if the zombies push the human in an anti-gravity like way, 
a zombie nearby is a greater threat than a zombie farther away.

####What about pathfinding?
Of course we use pathfinding, and we use it for when the zombies or humans are in a normal state, 
i.e. not chasing nor fleeing. The zombie or human are in that state given a random spot to go to, 
we use pathfinding to make it possible for it to go there without colliding with walls and so on.

The pathfinding algorithm we use are A*, read more about it here: http://en.wikipedia.org/wiki/A*_search_algorithm.

##What about the boring technical details?
###Game engine
We use libgdx as the game engine and Box2D as our physics engine.
