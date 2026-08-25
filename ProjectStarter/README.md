# Simplified Spaceflight Simulator

## Introduction

<p>This project is a very simple simulator used primarily for 
mapping the trajectories and orbits of rockets around a simplified
2D blue circle.
</p>

The project has rocket objects, which are: 
- launched from the surface of the circle
- controlled in direction and thrust by the user
- mapped to allow control of their trajectories
- have their trajectories controlled to orbit around the circle

To meet the Multiple satellites can be left on the map.

<p> The project can be used by anyone as a very simplified simulation 
to introduce the basics of orbital mechanics. I chose this project
because I am interested in aerospace engineering, and working on this
relevant project will help gain some basic level experience.
</p>

## User Stories

- As a user, I want to be able to add distance travel the rocket                         (done: Rocket.java)
- As a user, I want to be able to turn the rocket to different angles                    (done: Rocket.java)
- As a user, I want to be able to set the thrust of the rocket                           (done: Rocket. java)
- As a user, I want to be able to set the current trajectory based on speed and position (done: Trajectory.java)
- As a user, I want to be able to see the current trajectory based on speed and position (done: SimulationPanel.java)
- As a user, I want to be able to add each satellite into a list of satellites           (done: Earth.java)
- As a user, I want to be able to add more satellites to the map                (done: StartMenu/UserPanel/SimulatorApp)
- As a user, I want to be able to select the rocket to focus on, on the map     (done: UserPanel/Earth)
- As a user, I want to be able to remove satellites from the map                (done: UserPanel/Earth)
- As a user, I want to be able to see each satellite on the map                   (done: SimulationPanel/SimulatorApp)
- As a user, I want to be able to see the list of satellites                      (done: Earth.java/SimulatorApp.java)
- As a user, I want to be able to zoom in on the rocket I am viewing on the map   (done: UserPanel/SimulationPanel)
- As a user, I want to be able to speed up time on the simulation                 (done: UserPanel/SimulationPanel)

- As a user, I want to be able to save my rockets and their data to file               (done: JsonWriter/SimulationApp)
- As a user, I want to be able to load the rockets and their data from file and 
resume where they were left off                                                        (done: JsonReader/SimulationApp)

# Instructions for End User

- You start the simulation by running main, which starts with the start menu. There you start the simulation by creating a new Earth and adding a new rocket with a name you input, or load an earth from one of the files that you request for.
- You can locate my visual component by running main, then start a new simulation or load a simulation from the files, and it will automatically start a visual simulation.
- You can view the panel that displays the Rockets that have already been added to Earth by running the main simulation. The main simulation runs after the start menu. The names of rockets are found on the panel to the right of the simulation panel, and the selected rocket is under the list of rockets.
- You can add multiple Rockets to Earth by entering the name of your new rocket into the rocket names field, and click add rocket.
- You can select a Rocket to focus on in your Earth by entering the name of your new rocket into the rocket names field, and click select rocket.
- You can remove Rockets from Earth by entering the name of your new rocket into the rocket names field, and click remove rocket.
- You can view the multiple Rockets in Earth by seeing all of the rockets on the map, and in the list of names of the rockets in the user panel to the right of the map.
- You can view the specific data about the selected rocket in the panel to the right of the map including position, distance from ground, velocity and speed, thrust and turn angle.
- You can view the expected trajectory/orbital path of the rocket (which is the path of the rocket if the rocket's thrust is turned off) on the map.
- You can turn the selected rocket by clicking the "<" or ">" buttons to turn the angle of the rocket by a set change in degrees left or right once per click.
- You can set the thrust of the selected rocket by changing the slider to values between 0 and 100%, then click the thrust toggle button to turn off or on the thrust to the value on the slider.
- You can set the zoom on the rocket in the map by clicking the "+" or "-" buttons to zoom in or out of the map.
- You can set the speed of the simulation by clicking the "<<" or ">>" buttons to speed up or slow down the simulation to 1x, 2x, 5x, 10x, 25x, 50x, 100x, 250x, 500x, and 1000x.
- You can save the state of my application by entering the name of the file you would like to write to, and clicking the write file button.
- You can reload the state of my application by entering the name of the file you would like to read from, and clicking the read file button either during the simulation, or in the start menu.

## Phase 4: Task 2

Event Log 

Sat Nov 22 09:39:42 PST 2025
New rocket added and selected with name: Rocket 1

Sat Nov 22 09:40:12 PST 2025
New rocket added and selected with name: Rocket 2

Sat Nov 22 09:40:21 PST 2025
Set selected rocket to: Rocket 1

Sat Nov 22 09:40:27 PST 2025
Removed rocket with name: Rocket 1

Sat Nov 22 09:40:27 PST 2025
Selected rocket with name: Rocket 2

Sat Nov 22 09:40:40 PST 2025
Saved simulation to file

## Phase 3: Task 3

<p> One major design problem in the project is the coupling between classes, especially with the ui classes and the Earth class. A lot of the ui classes have an instance of the Earth class because there is a lot of updating the UI. Creating an Observable relationship or just refactoring would help a lot with the coupling. </p>

<p> 