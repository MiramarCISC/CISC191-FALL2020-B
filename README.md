# CISC191-FALL2020-B
Group B(ill and Ted's Excellent Group) Project  
Intermediate Java Programming  

Anita Cheung, Michael Andrews, Adrian Macaraig, Heidi Migita, Kenneth Rathbun  
San Diego Miramar College  
CISC191: Intermediate Java Programming  
Professor Andrew Huang, Professor Rabb Muhammad  

# Team Members and Roles
Anita Cheung  
Michael Andrews  
Adrian Macaraig  
Heidi Migita  
Kenneth Rathbun  

# Purpose and Justification
This is a single player game where you can navigate worlds, battle monsters, and collect treasures. Intended for individuals staying at home, struggling with the repurcussions of COVID-19, we hope that it will bring you some fun during these difficult times.

# Materials Required
- Workstation (per member)
- AWS Educate instances
- Github pro repository
- Intellij (or other IDE)
- Git
- Discord
- Canvas

# Investigation/Development/Experimentation
- Recursion: map generation
- Sorting: help path monsters
- GUI basics and UI controls: avatar navigation
- memory management/data structures: store the maps and the player’s inventory
- linkedlist/arraylist: threading

# Logistics
1. October 25: UML diagram/pseudocode
2. November 7:
    - map generation
    - player generation
    - monster generation
    - threading class
3. November 14: (depends on 2)
    - incorporate components
4. November 21: (depends on 3)
    - test and debug

# Draft 1: UML Planning
class Player {  
    - MAXHEALTHPOINTS: final int  
    - healthPoints: int  
    - numTreasures: int   
    - strength: int  
    - posX: int  
    - posY: int  
    + Player()  
    + getHealthPoints(): int  
    + setHealthPoints(int): void  
    + getNumTreasures(): int  
    + setNumTreasures(int): void  
    + attack(Monster): void  
    + usePotion(): void  
    + updateX(): void  
    + updateY(): void  
    + updatePosition(): void  
} 

class Grid {  
    + setBackground(): void   
    + setObject(): void  
    + actionWithExecutorService(): void 
} 

class Thread {  
    - healthPoints: int  
    - maxAttackStrength: int  
    - posX: int  
    - posY: int  
    + Monster()  
    + getHealthPoints(): int  
    + setHealthPoints(int)  
    + attack(Player): void  
    + updateX(): void  
    + updateY(): void  
    + updatePosition(): void  
}