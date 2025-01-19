Driver class:  
game/Catan  


Main classes to be commented:  
game/Catan  
game/Game  
game/Player  
board/Board  


All features are implemented exactly as according to catan_rules_almanac.pdf unless otherwise specified  
Please read catan_rules_almanac.pdf for complete list of niche rules and procedures implemented  
Notes about coordinate system: Tiles are specified by their top-most vertex coordinate when moving the robber  
Possible bugs because testing is difficult (probability based game states and time-consuming gameplay)  
As of right now, four-player is untested but no bugs or issues are forecasted  
How to change win condition (number of victory points to win): Change the final variable WIN in game/Game  

Features implemented:
1. Initial setup of settlements
Different feature: Each player received 2 brick and 2 lumber to build 2 roads on their turn, not in setup phase
2. Rolling dice and distribution of resources to each player
3. Robber when a 7 is rolled
Different feature: players with over 7 cards must discard half (rounded down), the discarding is done randomly
4. Building a road
5. Building a settlement
6. Building a city
7. Winning the game and output final score and rank

Features to be implemented:
1. Implement robber (move the robber and remove the resource distribution, steal card (?))
2. Implement trading (generic trade, player trade, trade on ports)
3. Implement development cards (year of plenty, monopoly, road building)
4. Implement longest road and largest army
