Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
We first converted the image into a matrix of pixels, with each pixel leading to
another pixel to its lower left, center and right. We then relaxed the vertices
in this matrix using Dijkstra's algorithm, with the weight of each path being
equal to the energy value of the vertex that it leads to. After relaxing all the
vertices, the algorithm parses the lowest row of pixels to see which one has the
shortest relaxed value, or "distance" to the top row. After that, the algorithm
finds the node with the shortest distance that leads up to that node it just
chose, and this process repeats until there is a path that leads from the top
row to the bottom row of the matrix.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
An image of a landscape would be suitable for the seam-carving approach, as
often times there are large areas of the image where there is less relevant
pixels, which means that these areas can be carved out if the image needs to be
resized. An image that would not work well would be any image with a lot of
items, like a picture of a busy city.


/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
800          0.349               -----       ---------
1600         0.617               1.768       0.822
3200         1.244               2.016       1.011
6400         2.785               2.239       1.163
12800        5.889               2.115       1.081
25600        12.194              2.071       1.050
51200        25.692              2.107       1.075


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
800          0.301              -----
1600         0.615              2.043         1.031
3200         1.287              2.093         1.066
6400         2.97               2.308         1.207
12800        6.263              2.109         1.077
25600        13.726             2.192         1.132
51200        29.942             2.181         1.125


/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 3.4*10^-8 * W^1.1 * H^1.1


Using our experimental data, we determined the exponents, which represented the
factors of growth for both H and W. We then determined the coefficients using
the formula T(n) = a * n^b in order to determine a for both variables. We then
multiplied our formulas for W and H in order to get our final tilde runtime
(multiplied coefficients as well).


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
The horizontal seam finding algorithm could be made faster, as it just
reimplements the vertical seam finding algorithm using transposed images. This
use of transposed images makes the algorithm not very performant because it has
to recreate the image twice (once to transpose, and once to reverse the
transposition).



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
Minimizing the number of energy calculations for each pixel was a challenge that
we enountered. It would often call this energy function multiple times for the
same pixels. We solved this issue by storing the energy calculations in
temporary variables. After optimizing the code in this way, the runtime of the
program increased.



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
n/a
