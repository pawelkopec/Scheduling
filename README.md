# Scheduling
## Description
This repository will include implementation of algotihms for scheduling of unit-length jobs with cubic incompatibility graphs.
Algorithms are based on article "Scheduling of unit-length jobs with cubic incompatibility graphs on three uniform machines" written by Hanna Furmańczyk and Marek Kubale. Article is available under a link:  

http://eti.pg.edu.pl/documents/174618/23783336/Scheduling%20of%20unit-length%20jobs%20with%20cubic%20incompatibility%20graphs%20on%20three%20uniform%20machines.pdf

## Scheduling problem
We are given n unit-length jobs. Each job is in conflict with exactly 3 other jobs. The goal is to find such a division of those jobs between 3 machines of given processing speeds that:
  1. No 2 jobs that are in conflict will be processed on the same machine.
  2. Overall processing time will be minimal.

This problem can be presented as finding an apropriate 3-coloring of cubic incompatibility graph. All the details are provided in the above mentioned article.
