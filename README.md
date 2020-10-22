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
- 

# Draft 1: UML Planning

## Class Player extends Thread
&#8211 MAXHEALTHPOINTS: final int  
&#8211 healthPoints: int  
'- numTreasures: int   
'- strength: int  
'- posX: int  
'- posY: int  
- + Player()
- + getHealthPoints(): int
- + setHealthPoints(int): void
- + getNumTreasures(): int
- + setNumTreasures(int): void
- + attack(Monster): void
- + usePotion(): void
- + updateX(): void
- + updateY(): void
- + updatePosition(): void

## Class Grid
- + setBackground(): void
- + setObject(): void
- + actionWithExecutorService(): void

## Class Monster extends Thread
- healthPoints: int
- maxAttackStrength: int
- posX: int
- posY: int
- + Monster()
- + getHealthPoints(): int
- + setHealthPoints(int)
- + attack(Player): void
- + updateX(): void
- + updateY(): void
- + updatePosition(): void