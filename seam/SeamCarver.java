import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;

public class SeamCarver {

    private Picture picture; // Instance variable for the SeamCarver object


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Invalid picture provided");
        this.picture = new Picture(picture.width(), picture.height());
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                this.picture.setRGB(i, j, picture.getRGB(i, j));
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture copy = new Picture(picture.width(), picture.height());
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                copy.setRGB(i, j, picture.getRGB(i, j));
            }
        }
        return copy;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException("Invalid coordinates");
        // mod operator to find the adjacent cells since energy wraps around
        int leftColor = 0, rightColor = 0, bottomColor = 0, topColor = 0;
        if (width() > 1) {
            leftColor = picture.getRGB((x - 1 + width()) % width(), y);
            rightColor = picture.getRGB((x + 1) % width(), y);
        }
        if (height() > 1) {
            bottomColor = picture.getRGB(x, (y - 1 + height()) % height());
            topColor = picture.getRGB(x, (y + 1) % height());
        }
        double horizontalGrad =
                Math.pow(red(rightColor) - red(leftColor), 2)
                        + Math.pow(green(rightColor) - green(leftColor), 2)
                        + Math.pow(blue(rightColor) - blue(leftColor), 2);
        double verticalGrad =
                Math.pow(red(topColor) - red(bottomColor), 2)
                        + Math.pow(green(topColor) - green(bottomColor), 2)
                        + Math.pow(blue(topColor) - blue(bottomColor), 2);
        return Math.sqrt(horizontalGrad + verticalGrad);

    }

    // private method to extract red component from getRGB()
    private int red(int color) {
        return (color >> 16) & 0xFF;
    }

    // private method to extract green component from getRGB()
    private int green(int color) {
        return (color >> 8) & 0xFF;
    }

    // private method to extract blue component from getRGB()
    private int blue(int color) {
        return (color) & 0xFF;
    }

    // transposes the input picture
    private void transpose(Picture pic) {
        Picture transpose = new Picture(pic.height(), pic.width());
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                transpose.setRGB(j, i, pic.getRGB(i, j));
            }
        }
        picture = transpose;

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose(picture);
        int[] horizontalSeam = findVerticalSeam();
        transpose(picture);
        return horizontalSeam;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (width() == 1) {
            int[] zeros = new int[height()];
            for (int i = 0; i < zeros.length; i++)
                zeros[i] = 0;
            return zeros;
        }
        else if (height() == 1) {
            double minEnergy = Double.POSITIVE_INFINITY;
            int minIndex = -1;
            for (int i = 0; i < width(); i++) {
                double energyPixel = energy(i, 0);
                if (energyPixel < minEnergy) {
                    minEnergy = energyPixel;
                    minIndex = i;
                }
            }
            int[] min = { minIndex };
            return min;
        }
        else {
            double[][] energy = new double[picture.height()][picture.width()];
            int[] verticalSeam = new int[energy.length];
            // the below code relaxes all the vertices
            for (int j = 0; j < picture.width(); j++) {
                energy[0][j] = energy(j, 0);
            }

            for (int i = 0; i < energy.length - 1; i++) {
                double left = -1;
                double down = -1;
                for (int j = 0; j < energy[i].length; j++) {

                    if (j == 0) {
                        left = energy(j, i + 1);
                        down = energy(j + 1, i + 1);
                        energy[i + 1][j] = energy[i][j] + left;
                        energy[i + 1][j + 1] = energy[i][j] + down;
                    }
                    if (j != 0) {

                        if (energy[i][j] + left
                                < energy[i + 1][j - 1])
                            energy[i + 1][j - 1] = energy[i][j]
                                    + left;
                        if (energy[i][j] + down
                                < energy[i + 1][j])
                            energy[i + 1][j] = energy[i][j]
                                    + down;

                        left = down;

                    }
                    if (j != energy[i].length - 1) {
                        down = energy(j + 1, i + 1);
                        energy[i + 1][j + 1] = energy[i][j] + down;
                    }


                }
            }

            // finds Vertical seam
            double temp = energy[energy.length - 1][0];
            int championCol = 0;
            for (int k = 0; k < energy[energy.length - 1].length; k++) {
                if (energy[energy.length - 1][k] < temp) {
                    championCol = k;
                    temp = energy[energy.length - 1][k];
                }
            }

            // add the first node to vertical seam
            verticalSeam[verticalSeam.length - 1] = championCol;


            for (int i = energy.length - 1; i > 0; i--) {
                if (championCol == 0) {
                    if (energy[i - 1][championCol + 1] <
                            energy[i - 1][championCol]) {
                        championCol += 1;
                    }

                    verticalSeam[i - 1] = championCol; // where the next
                    // node gets added

                }
                else if (championCol == energy[i - 1].length - 1) {

                    if (energy[i - 1][championCol - 1] <
                            energy[i - 1][championCol]) {
                        championCol -= 1;
                    }

                    verticalSeam[i - 1] = championCol;

                }
                else {
                    temp = energy[i - 1][championCol - 1];
                    int tempAddition = -1;
                    for (int k = -1; k <= 1; k++) {
                        if (energy[i - 1][championCol + k] <= temp) {
                            temp = energy[i - 1][championCol + k];
                            tempAddition = k;
                        }
                    }
                    championCol += tempAddition;

                    verticalSeam[i - 1] = championCol;
                }

            }


            return verticalSeam;

        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() == 1 || seam == null || seam.length != picture.width())
            throw new IllegalArgumentException("Invalid parameters provided.");
        transpose(picture);
        removeVerticalSeam(seam);
        transpose(picture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() == 1 || seam == null || seam.length != picture.height()) {
            throw new IllegalArgumentException("Invalid parameters provided.");
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("Invalid Seam");
        }
        Picture verticalRemoved = new Picture(picture.width() - 1,
                                              picture.height());

        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                if (j < seam[i])
                    verticalRemoved.setRGB(j, i, picture.getRGB(j, i));
                else if (j > seam[i])
                    verticalRemoved.setRGB(j - 1, i, picture.getRGB(j, i));
            }
        }
        picture = verticalRemoved;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture1 = new Picture("chameleon.png");
        Picture picture2 = new Picture("1x8.png");
        SeamCarver sc = new SeamCarver(picture1);
        SeamCarver sc2 = new SeamCarver(picture2);
        Picture picture = sc.picture();
        picture.show();
        for (int i = 0; i < sc2.width(); i++)
            for (int j = 0; j < sc2.height(); j++)
                sc2.energy(i, j);
        int[] verticalSeam = sc.findVerticalSeam();
        // for (int i = 0; i < verticalSeam.length; i++) {
        //     StdOut.print(verticalSeam[i] + " ");
        // }
        // StdOut.println();
        sc.removeVerticalSeam(verticalSeam);
        int[] horizontalSeam = sc.findHorizontalSeam();
        // for (int i = 0; i < horizontalSeam.length; i++) {
        //     StdOut.print(horizontalSeam[i] + " ");
        // }
        sc.removeHorizontalSeam(horizontalSeam);


        int width = 51200;
        int height = 2000;

        Picture randomPicture = new Picture(width, height);

        for (int i = 0; i < randomPicture.width(); i++) {
            for (int j = 0; j < randomPicture.height(); j++) {
                int red = StdRandom.uniformInt(255);
                int blue = StdRandom.uniformInt(255);
                int green = StdRandom.uniformInt(255);
                randomPicture.set(i, j, new Color(red, green, blue));
            }
        }
        SeamCarver sc3 = new SeamCarver(randomPicture);
        Stopwatch stopwatch = new Stopwatch();
        int[] horizontalSeam2 = sc3.findHorizontalSeam();
        sc3.removeHorizontalSeam(horizontalSeam2);
        int[] verticalSeam2 = sc3.findVerticalSeam();
        sc3.removeVerticalSeam(verticalSeam2);
        StdOut.println(stopwatch.elapsedTime());
        // randomPicture.show();
    }

}
