import random
import math  # cos() for Rastrigin
import copy  # array-copying convenience
import sys  # max float

def show_vector(vector):
    for i in range(len(vector)):
        if i % 8 == 0:  # 8 columns
            print("\n", end="")
        if vector[i] >= 0.0:
            print(' ', end="")
        print("%.4f" % vector[i], end="")  # 4 decimals
        print(" ", end="")
    print("\n")


#Berechnung der Abweichung reduziert auf eine Dimension
#f(xi) = 10*1(Anzahl Dimensionen) + ((xi*xi)-(10*cos(2*pi*xi)))
#f(xi) = err
def error(position):
    err = 0.0
    for i in range(len(position)):
        xi = position[i]
        err += (xi * xi) - (10 * math.cos(2 * math.pi * xi)) + 10
    return err

#Defintion eines Partikels
#Jeder Partikel kann sich an seine beste Position bzw. Abweichung erinnern
class Particle:
    def __init__(self, dim, minxyz, maxxyz):
        self.rnd = random.Random()
        self.position = [0.0 for i in range(dim)]
        self.velocity = [0.0 for i in range(dim)]
        self.best_part_pos = [0.0 for i in range(dim)]

        for i in range(dim):
            self.position[i] = ((maxxyz - minxyz) *
                                self.rnd.random() + minxyz)
            self.velocity[i] = ((maxxyz - minxyz) *
                                self.rnd.random() + minxyz)

        self.error = error(self.position)
        self.best_part_pos = copy.copy(self.position)
        self.best_part_err = self.error

def Solve(max_epochs, n, dim, minx, maxx):

    # Erschaffen von n Partikeln
    swarm = [Particle(dim, minx, maxx) for i in range(n)]
    best_swarm_pos = [0.0 for i in range(dim)]
    best_swarm_err = sys.float_info.max
    for i in range(n):  # check each particle
        if swarm[i].error < best_swarm_err:
            best_swarm_err = swarm[i].error
            best_swarm_pos = copy.copy(swarm[i].position)

    epoch = 0
    w = 0.729  # inertia
    c1 = 1.49445  # cognitive (particle)
    c2 = 1.49445  # social (swarm)

    while epoch < max_epochs:
        # Prüfung jedes Partikels
        for i in range(n):
            # Berechne neue Velocity für jeden Partikel bisher [0,0,0]
            for k in range(dim):
                r1 = random.random()
                r2 = random.random()
                #siehe Buch Advances in Swarm Intelligence Seite 41
                #velocity = w * velocity + c1 * r1 * (beste Partikelposition - derzeitige Partikelposition) + c2 * r2* (beste Schwarmposition - dezeitige Partikelposition)
                swarm[i].velocity[k] = ((w * swarm[i].velocity[k]) +
                                        (c1 * r1 * (swarm[i].best_part_pos[k] -
                                                    swarm[i].position[k])) +
                                        (c2 * r2 * (best_swarm_pos[k] -
                                                    swarm[i].position[k])))
                #Eingrenzung der Velocity für jeden Partikel auf den maximalen Funktionsraum
                if swarm[i].velocity[k] < minx:
                    swarm[i].velocity[k] = minx
                elif swarm[i].velocity[k] > maxx:
                    swarm[i].velocity[k] = maxx

            # Berechnung der neuen Position eines Partikels mit der neuen Velocity
            for k in range(dim):
                swarm[i].position[k] += swarm[i].velocity[k]

            # is new position a new best for the particle?
            if swarm[i].error < swarm[i].best_part_err:
                swarm[i].best_part_err = swarm[i].error
                swarm[i].best_part_pos = copy.copy(swarm[i].position)

            # is new position a new best overall?
            if swarm[i].error < best_swarm_err:
                best_swarm_err = swarm[i].error
                best_swarm_pos = copy.copy(swarm[i].position)

        epoch += 1

    return best_swarm_pos

print("\nBegin particle swarm optimization using Python demo\n")
dim = 3
print("Goal is to solve Rastrigin's function in " + str(dim) + " variables")
print("Function has known min = 0.0 at (", end="")
for i in range(dim-1):
  print("0, ", end="")
print("0)")

num_particles = 1000
max_epochs = 15000

print("Setting num_particles = " + str(num_particles))
print("Setting max_epochs    = " + str(max_epochs))
print("\nStarting PSO algorithm\n")

best_position = Solve(max_epochs, num_particles,
 dim, -10.0, 10.0)

print("\nPSO completed\n")
print("\nBest solution found:")

show_vector(best_position)
err = error(best_position)

print("Error of best solution = %.6f" % err)
print("\nEnd particle swarm demo\n")