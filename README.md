## Decision Support System

#### Decision Support System is a simple program which solve some of the most important question "take off or not", because of one of the most dangerous parts of the flight is a take off. This application is based on an arcraft flight manual An-12(http://www.aviadocs.net/RLE/An-12/CD1/RLYE/An-12_RLYE.pdf). So, answers are correct only for An-12.
#### Initial data
* the size of the runway, stopped behind after a start in the moment of decision-making (at least 500 m);
* the number of working engines (at least three)
* flap deviation angle (for runways covered with artificial surface - at least 15 °,
* for unpaved or snowy runways - at least 25 °)
* condition of the runway.
#### The program analyses findings and gives out a decision on continuation of flight or his stopping - "ЗЛІТАТИ" (take off) or "НЕ ЗЛІТАТИ" (not take off).

#### Main idea: using neural network as a simple artificial intelligence. This app uses FANN (http://leenissen.dk/fann/wp/) 
