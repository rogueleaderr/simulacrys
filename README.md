= README

This project is a (very early stage) attempt to create a realistic,
high fidelity simulation of a functional economic (and, in time, perhaps also political) system.

The original impetus for this project was to assist in building a believable world for an original Dungeons and Dragons
campaign setting. As I started imagining a fictional world for my friends' characters to inhabit, I got increasingly
obsessed and concerned with how to construct a world that felt natural and alive. As a very experienced video gamer
and consumer of various fantastical movies, books, and assorted media I'm well aware that the is a constant tension
between realism in setting and convenience for both storytelling (inasmuch as characters cannot keep track of nearly
as many people and places as exist in the real world), and construction (inasmuch as world with too many people is
expensive to design and depict).

Nevertheless, as a professional programmer, erstwhile macroeconomic analyst, and an inveterate obsessive I've found myself
drawn away from crafting a story and towards the challenge of crafting a world. And so herein you'll find my best attempt
to develop a computer program populated with people who behave a realistically as possible to achieve goals by means of
definite actions in a a concrete environment.

Because the goal is to create emergent macro-economic behavior that is nevertheless visible and interpretable at a
"human scale" (I do eventually want to bring player characters into this world), it relies on agent-based modeling
techniques rather than classical macro-economic modeling. The fact that I know hardly anything about agent-based modeling
or about classical macro-economic modeling will hopefully not be too great a barrier.

== Architecture

This project doubles a convenient excuse for me to learn Scala. I'm particularly interested in learning (and learning) from the functional aspects of Scala, so I set a goal of building this simulation using (in as much as possible), exclusively **pure functional** programming, i.e. completely avoiding mutation. So far, I've managed to avoid all mutability, even though that's made the design significantly more complex and probably less efficient. I haven't gotten full purity because this type of simulation leans very heavily on random number generation and *purifying* my RNG usage hasn't felt worthwhile.

The nice thing about this design is that it, at least in principle, allows for full parallelization of agent decision making (though my current design requires a full bottlenecking "reduce" step between each tick of the game clock).

In the future, I hope to allow the agents to **learn** policies through some type of reinforcement learning.

== The Domain

I will attempt to model a single "closed" economic system consisting effectively of a single fictional country. In the future,
I may try to model a system with multiple countries (and thus exchange rates) but in my first iteration I'll stick to one.

==== Locations

I'll refer to this country as *"The Kingdom"*. The kingdom will consist of a continuous geographic expanse which I will model
as a square grid of 100,000x100,000 *hectares*. Each hectare will have exactly one type of primary economic usage,
e.g. agricultural, light industrial, educational, fortified, etc. A *location* is a conjoined set of hectares with some
common unified significance, e.g. *a village*, *a town*, *a city*, *a farm*, etc.

==== Inhabitants

The kingdom will be occupied by *People*. People, like in the real world, will have definite demographic characteristics
including age, gender, height, and weight. They will have *needs* such as eating, drinking, and shelter. And they will
have desires such as wealth and...well, for simplicity primarily wealth.

People will be organized into *Families*. The simulation will start with a pre-existing populace and then the populace
will expand as *Children* are born and contract as people *Die*.

People who are not able to satisfy their basic needs will die. So each person will be programmed with a core set of
*Policies* that guide them to take *Actions* which promote survival.

==== Time

As in life, time in the simulation flies relentlessly forward. Time in the simulation is governed by a single global
clock that moves forward in five minute ticks. At the beginning of each tick, each person will make a decision based on
their policies about which action to take over the coming period (which choice may be simply to continue a previously undertaken action which requires more than an hour to complete). At the beginning of the next tick, each person will observe the
results of their action on the previous tick, observe the current state of their locally observable environment, and
use that information to choose an action for the next round.

The simulation will progress indefinitely until everyone in the Kingdom dies.
