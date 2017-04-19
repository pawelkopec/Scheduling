# Scheduling
## Description
This repository will include implementation of algorithms for scheduling of unit-length jobs with incompatibility graphs of bounded degree.
Cubic scheduling algorithms are based on article "Scheduling of unit-length jobs with cubic incompatibility graphs on three uniform machines" written by Hanna Furmańczyk and Marek Kubale. Article is available under a link:  

http://eti.pg.edu.pl/documents/174618/23783336/Scheduling%20of%20unit-length%20jobs%20with%20cubic%20incompatibility%20graphs%20on%20three%20uniform%20machines.pdf

## Scheduling problem
We are given n unit-length jobs and processing speeds of 3 or 4 machines working in parallel. Each job is in conflict with 3, 4 or less other jobs, depending on the particular problem. The goal is to find such a division of those jobs between machines that:
  1. No 2 jobs that are in conflict will be processed on the same machine.
  2. Processing time will be minimal.

This problem can be presented as finding an appropriate 3-coloring of cubic incompatibility graph. All the details are provided in the above mentioned article.

## Packages
  * __graph__ package will contain necessary abstractions for building graph algorithms alongside with basic implementations of those abstractions (neighbour list graph, adjacency matrix graph, vertex coloring class etc.)  **[will be soon moved to another repo and used in other project]**
  * __scheduling__ package will contain classes needed specifically for this scheduling problem

## Status
In production. Not for use.   

First algorithm is not yet complete, but works for almost all graphs. Errors occur sometimes for graphs of n < 100. So far my modified implementation seems to have improved complexity from quadratic to linear.

Feedback welcome.
