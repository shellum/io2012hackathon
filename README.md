io2012hackathon
===============

Google IO 2012 Hackathon to solve social challenges: Go Green - Croud source real MPG for vehicles depending on location

For the hackathon, the project started with three people at an extended google io event.
The purpose of this code is to allow participants to see what the average person in their area really gets for a MPG given their vehicle. It's an idea similar to benchmarking devices. The more people contributing real live information, the more accurate the information will be. This is especially true given that driving habits and conditions differ greatly depending on location.

Use case: Someone with an android phone fills up their gas tank. They open up the android app and login just using their email address as an identifier. They then enter their mileage, number of gallons they filled up with, and an auto-calculated date/GPS coordinate pair. The app then sends this data to the main datastore. Later, after many people have contributed this kind of data, they load the website, and can browse for a given city what the average MPG is for a make/model/year/etc of vehicle. Reports could be run to show MPG differences for cities that are mostly city vs those with highways, and these costs could be factored into cost of living expenses. This data could also be used to show which cities really are greener than others.

This project is made up of three parts:
A server backend (that can run on Google's AppEngine). This is the main datastore.
A frontend, that allows data to be entered probably from desktop/laptop computers.
A frontend (that can be run from Android devices), that allows data to be entered from mobile devices like phones.

The initial work took place during the hackathon which lasted roughly four hours. The base apps exist, with communication taking place between them. There are however 1-2 more web service calls that must be added in order for the apps to work/become useful. The infrastructure however, is there and functioning. Each of the three participants worked on one of the three items listed above such that work could be done in parallel. This is the reason that they are not starting out completely integrated, and have some redundancy.

Overall, there are two main domain model objects:
Vehicle (make, model, year, vin)
Fillup data (gallons, mileage, date, gps coordinates)

