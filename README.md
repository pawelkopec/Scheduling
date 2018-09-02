# Scheduling [OBSOLETE]

This repo was used for computational experiments for this article: https://journals.agh.edu.pl/dmms/article/view/2502. The algorithm described runs in O(n^2), but I found a way simpler solution in O(n), which coauthor of the article Szymon Duraj used in MSc thesis. I did not find a public link to it though. I regard this repo as obsolete because of improved algorithm complexity, but I am leaving it as a souvenir.

## Description
This repository will include implementation of algorithms for scheduling of unit-length jobs with incompatibility graphs of bounded degree.
Cubic scheduling algorithms are based on article "Scheduling of unit-length jobs with cubic incompatibility graphs on three uniform machines" written by Hanna Furmańczyk and Marek Kubale. Article is available under a link:

http://eti.pg.edu.pl/documents/174618/23783336/Scheduling%20of%20unit-length%20jobs%20with%20cubic%20incompatibility%20graphs%20on%20three%20uniform%20machines.pdf

## Scheduling problem
We are given n unit-length jobs and processing speeds of 3 or 4 machines working in parallel. Each job is in conflict with 3, 4 or less other jobs, depending on the particular problem. The goal is to find such a division of those jobs between machines that:
  1. No 2 jobs that are in conflict will be processed on the same machine.
  2. Processing time will be minimal.

This problem can be presented as finding an appropriate 3-coloring of cubic incompatibility graph. All the details are provided in the above mentioned article.

## Status
No sense in using. Algorithm got better.
